package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanMvMapper;
import com.zte.clonedata.model.DoubanMv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:46 2020/5/28
 * @Description:
 */
@Slf4j
@Component
public class JobDoubanMv {
    @Autowired
    private DoubanMvMapper doubanMvMapper;
    /**
     * 切割集合并发访问因子
     */
    private static final int spList = 20;
    private int c = 0;
    private static Map<String, Integer> startMap = new HashMap<>();

    public String execute(String counrty,String year1,String year2) throws InterruptedException {
        /*if (counrty.length()>0){
            return "OK";
        }*/
        String key = counrty.concat(year1).concat(year2);
        ExecutorService exe = Executors.newCachedThreadPool();
        log.info("豆瓣开始执行任务   >>>");
        //检查主目录
        checkBasePath();
        List<DoubanMv> doubanMvList = Lists.newArrayList();
        PicDownUtils picDownUtils = new PicDownUtils();
        boolean isLock = false;
        String executeResult = null;
        int thisc = 0;
        synchronized (this) {
            Integer start = startMap.get(key) == null ? 0 : startMap.get(key);
            while (true) {
                String url = String.format(
                        "https://movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=电影&countries=%s&year_range=%s,%s&start=%s",
                        counrty, year1, year2, start);
                try {
                    getDoubanMvList(url, picDownUtils, doubanMvList);
                    log.info("start => {}", start);
                } catch (BusinessException e) {
                    if (e.getCommonError().getErrorCode() == 20002) {
                        break;
                    }
                    isLock = true;
                    executeResult = "发生错误! 可能原因: ".concat(e.getCommonError().getErrorMsg());
                    break;
                } catch (Exception e){
                    isLock = true;
                    executeResult = "发生错误! 可能原因: ".concat(e.getMessage());
                    break;
                }
                if (thisc == 500){
                    isLock = true;
                    log.info("此次收集电影信息已达500,暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}", counrty, year1, year2);
                    break;
                }
                if (start == 3000) {
                    log.info("此段收集电影信息已达3000,结束此段任务 ... >>> country: {}, year: {}-{}", counrty, year1, year2);
                    break;
                }
                start = start + 20;
                thisc = thisc + 20;
                Thread.sleep(30000);
            }
            if (!isLock) {
                startMap.put(key, 0);
            } else {
                startMap.put(key, start);
            }
            log.info("豆瓣 {} 条数据加载完毕,即将爬取这些数据的详情页面   >>>", doubanMvList.size());
            if (picDownUtils.files.size() != 0) {
                Thread t1 = new Thread(picDownUtils);
                exe.execute(t1);
            }
            saveMovice(doubanMvList, exe);
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    break;
                }
                Thread.sleep(500);
            }
        }
        if (executeResult == null) {
            executeResult = String.format("请求成功,新增电影: %s 条", JobDoubanMvDetail.successCount);
        } else {
            executeResult = String.format("请求成功,新增电影: %s 条, 请求过程中%s", JobDoubanMvDetail.successCount, executeResult);
        }
        JobDoubanMvDetail.successCount = 0;
        log.info(executeResult);
        return executeResult;
    }

    private void checkBasePath() {
        File baseFile = new File(Contanst.BASEURL.concat(Contanst.TYPE_DOUBAN));
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
    }

    private void getDoubanMvList(String url, PicDownUtils picDownUtils, List<DoubanMv> doubanMvList) throws InterruptedException, BusinessException {
        try {
            String result = HttpUtils.getJson(url, Contanst.DOUBAN_HOST1);
            if (result.length() == 11) {
                throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
            } else if (result.contains("检测到有异常请求从您的IP发出")) {
                throw new BusinessException(EmBusinessError.HTTP_RESULT_IPLOCK);
            }
            List<DoubanModel> doubanModelList = JSONObject.parseArray(result.substring(8, result.length() - 1), DoubanModel.class);
            for (DoubanModel doubanModel : doubanModelList) {
                String imageurl = doubanModel.getCover();
                String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
                String path = Contanst.BASEURL.concat(Contanst.TYPE_DOUBAN).concat(File.separator).concat(name);
                DoubanMv mv = doubanMvMapper.selectByPrimaryKey(doubanModel.getId());
                if (mv == null) {
                    //不存在
                    DoubanMv doubanMv = new DoubanMv();
                    doubanMv.setUrl(doubanModel.getUrl());
                    doubanMv.setHttpImage(imageurl);
                    doubanMv.setFilepath(path);
                    doubanMv.setMovieid(doubanModel.getId());
                    doubanMv.setRatingnum(doubanModel.getRate());
                    doubanMv.setMoviename(doubanModel.getTitle());
                    doubanMvList.add(doubanMv);
                } else {
                    if (!mv.getRatingnum().equals(doubanModel.getRate())) {
                        //分数不同
                        DoubanMv doubanMv = new DoubanMv();
                        doubanMv.setMovieid(doubanModel.getId());
                        doubanMv.setRatingnum(doubanModel.getRate());
                        doubanMvMapper.updateByPrimaryKeySelective(doubanMv);
                    }
                }

                /**
                 * 1. 存在   不处理
                 * 2. 不存在 加入任务
                 */
                File file = new File(path);
                if (file.exists()) {
                    continue;
                } else {
                    picDownUtils.urls.add(imageurl);
                    picDownUtils.files.add(file);
                }
            }
        } catch (BusinessException e) {
            if (e.getCommonError().getErrorCode() == 20002 || e.getCommonError().getErrorCode() == 20003) {
                throw e;
            }
            if (c++ < 10) {
                log.error("发生错误url >>> {}", url);
                log.error("30秒后再次尝试连接  >>>{}<<<", c);
                Thread.sleep(30000);
                getDoubanMvList(url, picDownUtils, doubanMvList);
            } else {
                throw e;
            }
        } finally {
            c = 0;
        }
    }


    private void saveMovice(List<DoubanMv> movies, ExecutorService exe) {
        int size = movies.size() / spList;
        int b = movies.size() % spList;
        log.info("计划创建: {}条任务  >>>", b == 0 ? size : (size + 1));
        log.info("开始执行计划任务项  >>>");
        for (int i = 0; i < size; i++) {
            List<DoubanMv> m1 = new ArrayList<>(movies.subList((i * spList), (i + 1) * spList));
            exe.execute(new JobDoubanMvDetail(m1, doubanMvMapper));
        }
        if (b != 0) {
            List<DoubanMv> m1 = new ArrayList<>(movies.subList(size * spList, movies.size()));
            exe.execute(new JobDoubanMvDetail(m1, doubanMvMapper));
        }
    }

    public static void main(String[] args) {

    }

}

@Data
class DoubanModel {
    private String rate;
    private String url;
    private String cover;
    private String id;
    private String title;
}