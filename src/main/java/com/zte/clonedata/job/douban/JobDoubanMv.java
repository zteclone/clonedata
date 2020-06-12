package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanMvMapper;
import com.zte.clonedata.dao.TaskLogMapper;
import com.zte.clonedata.model.DoubanMv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private volatile int c = 0;
    /**
     * 切割集合并发访问因子
     */
    private static final int spList = 25;

    public String execute() throws InterruptedException {
        ExecutorService exe = Executors.newCachedThreadPool();
        log.info("豆瓣开始执行任务   >>>");
        //检查主目录
        checkBasePath();
        List<DoubanMv> doubanMvList = Lists.newArrayList();
        int i = 0;
        PicDownUtils picDownUtils = new PicDownUtils();
        boolean success = true;
        String executeResult = "";
        synchronized (this) {
            while (true) {
                try {
                    getDoubanMvList(i, picDownUtils,doubanMvList);
                } catch (BusinessException e) {
                    success = false;
                    executeResult = "失败,可能原因: ".concat(e.getCommonError().getErrorMsg());
                    break;
                }
                if (doubanMvList.size() != (i + 1000)) break;
                i = i + 1000;
            }
            if (success){
                log.info("豆瓣 {} 条数据加载完毕   >>>", doubanMvList.size());
                Thread t1 = new Thread(picDownUtils);
                exe.execute(t1);
                saveMovice(doubanMvList, exe);
                exe.shutdown();
                while (true) {
                    if (exe.isTerminated()) {
                        break;
                    }
                    Thread.sleep(500);
                }
                executeResult = "请求成功,更新电影: ".concat(String.valueOf(doubanMvList.size())).concat("条");
            }
            log.info(executeResult);
            return executeResult;
        }
    }

    private void checkBasePath() {
        File baseFile = new File(Contanst.BASEURL.concat(Contanst.TYPE_DOUBAN));
        if (!baseFile.exists()){
            baseFile.mkdirs();
        }
    }

    private void getDoubanMvList(int i, PicDownUtils picDownUtils,List<DoubanMv> doubanMvList) throws InterruptedException, BusinessException {
        String url = null;
        try {
            url = "https://movie.douban.com/j/search_subjects?type=movie&tag=热门&page_limit="
                    .concat(String.valueOf(i + 1000))
                    .concat("&page_start=")
                    .concat(String.valueOf(i));
            String result = HttpUtils.getJson(url, Contanst.DOUBAN_HOST1);
            String data = JSONObject.parseObject(result, Map.class).get(Contanst.JSON_KEY_DOUBAN).toString();
            List<DoubanModel> doubanModelList = JSONObject.parseArray(data, DoubanModel.class);
            for (DoubanModel doubanModel : doubanModelList) {
                String imageurl = doubanModel.getCover();
                String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
                String path = Contanst.BASEURL.concat(Contanst.TYPE_DOUBAN).concat(File.separator).concat(name);
                DoubanMv mv = doubanMvMapper.selectByPrimaryKey(doubanModel.getId());
                if (mv == null){
                    //不存在
                    DoubanMv doubanMv = new DoubanMv();
                    doubanMv.setUrl(doubanModel.getUrl());
                    doubanMv.setHttpImage(imageurl);
                    doubanMv.setFilepath(path);
                    doubanMv.setMovieid(doubanModel.getId());
                    doubanMv.setRatingnum(doubanModel.getRate());
                    doubanMvList.add(doubanMv);
                }else {
                    if (!mv.getRatingnum().equals(doubanModel.getRate())){
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
            if (c++ < 10) {
                log.error("发生错误url >>> {}", url);
                log.error("三秒后再次尝试连接  >>>{}<<<", c);
                Thread.sleep(3000);
                getDoubanMvList(i, picDownUtils,doubanMvList);
            } else {
                throw e;
            }
        }finally {
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

}
@Data
class DoubanModel{
    private String rate;
    private String url;
    private String cover;
    private String id;
}