package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.job.model.DoubanModel;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.util.HttpUtils;
import com.zte.clonedata.util.PicDownUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:46 2020/5/28
 * @Description:
 */
@Slf4j
@Component
@Async("taskExecutor")
public class JobDoubanTv extends AbstractJob {
    @Autowired
    private DoubanTvMapper doubanTvMapper;

    private int c = 0;
    private static Map<String, Integer> startMap = new HashMap<>();

    public String execute(String counrty, String year1, String year2) throws InterruptedException {
        String key = counrty.concat(year1).concat(year2);
        ExecutorService exe = Executors.newCachedThreadPool();
        log.info("豆瓣开始执行任务   >>>");
        //检查主目录
        checkBasePath(Contanst.BASEURL.concat(Contanst.TYPE_DOUBAN));
        Map<String, Mv> doubanTvMap = Maps.newHashMap();
        PicDownUtils picDownUtils = new PicDownUtils();
        boolean isLock = false;
        String executeResult = null;
        int thisc = 0;
        synchronized (this) {
            Integer start = startMap.get(key) == null ? 0 : startMap.get(key);
            while (true) {
                String url = String.format(
                        "https://movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=电视剧&countries=%s&year_range=%s,%s&start=%s",
                        counrty, year1, year2, start);
                try {
                    getListByURL(url, picDownUtils, doubanTvMap);
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
                if (thisc >= 500){
                    isLock = true;
                    log.info("此次收集电影信息已达500,暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}", counrty, year1, year2);
                    break;
                }
                if (start >= 3000) {
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
            log.info("豆瓣 {} 条数据加载完毕,即将爬取这些数据的详情页面   >>>", doubanTvMap.size());
            if (picDownUtils.files.size() != 0) {
                Thread t1 = new Thread(picDownUtils);
                exe.execute(t1);
            }
            executeDetail(doubanTvMap, exe);
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    break;
                }
                Thread.sleep(500);
            }
        }
        if (executeResult == null) {
            executeResult = String.format("请求成功,新增电影: %s 条", JobDoubanTvDetail.successCount);
        } else {
            executeResult = String.format("请求成功,新增电影: %s 条, 请求过程中%s", JobDoubanTvDetail.successCount, executeResult);
        }
        JobDoubanTvDetail.successCount = 0;
        log.info(executeResult);
        return executeResult;
    }

    protected <T> void getListByURL(String url, PicDownUtils picDownUtils, Map<String, T> dataMap) throws InterruptedException, BusinessException {
        try {
            String result = HttpUtils.getJson(url, Contanst.DOUBAN_HOST1,"");
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
                DoubanTv tv = doubanTvMapper.selectByPrimaryKey(doubanModel.getId());
                if (tv == null) {
                    //不存在
                    DoubanTv doubanTv = new DoubanTv();
                    doubanTv.setUrl(doubanModel.getUrl());
                    doubanTv.setHttpImage(imageurl);
                    doubanTv.setFilepath(path);
                    doubanTv.setTvid(doubanModel.getId());
                    doubanTv.setRatingnum(doubanModel.getRate());
                    doubanTv.setTvname(doubanModel.getTitle());
                    dataMap.put(doubanModel.getId(), (T) doubanTv);
                } else {
                    if (!tv.getRatingnum().equals(doubanModel.getRate())) {
                        //分数不同
                        DoubanTv doubanTv = new DoubanTv();
                        doubanTv.setTvid(doubanModel.getId());
                        doubanTv.setRatingnum(doubanModel.getRate());
                        doubanTvMapper.updateByPrimaryKeySelective(doubanTv);
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
                getListByURL(url, picDownUtils, dataMap);
            } else {
                throw e;
            }
        } finally {
            c = 0;
        }
    }

    protected <T> void executeDetail(Map<String, T> dataMap, ExecutorService exe) {
        int size = dataMap.size() / spList;
        int b = dataMap.size() % spList;
        log.info("计划创建: {}条任务  >>>", b == 0 ? size : (size + 1));
        log.info("开始执行计划任务项  >>>");
        /**
         * 防止一次任务多个相同id查出后,查询详情插入数据库的主键错误
         */
        List<DoubanTv> tvs = (List<DoubanTv>) dataMap.values().stream().collect(Collectors.toList());
        for (int i = 0; i < size; i++) {
            List<DoubanTv> m1 = new ArrayList<>(tvs.subList((i * spList), (i + 1) * spList));
            exe.execute(new JobDoubanTvDetail(m1, doubanTvMapper));
        }
        if (b != 0) {
            List<DoubanTv> m1 = new ArrayList<>(tvs.subList(size * spList, tvs.size()));
            exe.execute(new JobDoubanTvDetail(m1, doubanTvMapper));
        }
    }

}