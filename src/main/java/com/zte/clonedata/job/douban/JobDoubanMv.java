package com.zte.clonedata.job.douban;

import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
public class JobDoubanMv extends AbstractJob {

    public String execute(String counrty, String year1, String year2) throws InterruptedException {
        String key = "豆瓣电影 - ".concat(counrty).concat(year1).concat(year2);
        ExecutorService exe = Executors.newCachedThreadPool();
        log.info("{} 开始执行任务   >>>",key);
        Map<String, Mv> doubanMvMap = Maps.newHashMap();
        PicDownUtils picDownUtils = new PicDownUtils();
        boolean isLock = true;
        String executeResult = null;
        int thisc = 0;
        synchronized (this) {
            //查询该任务的起始页数
            PageNo pageNo = PageNo.builder().key(key).type(RunningContanst.TYPE_DOUBAN_ID).build();
            int start = Integer.parseInt(pageNoService.getValueByKeyAndTypeid(pageNo));
            while (true) {
                String url = String.format(
                        "https://movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=电影&countries=%s&year_range=%s,%s&start=%s",
                        counrty, year1, year2, start);
                try {
                    convertStringToObject.getDoubanMv(url,picDownUtils,doubanMvMap);
                } catch (BusinessException e) {
                    if (e.getCommonError().getErrorCode() == 20002) {
                        isLock = false;
                        break;
                    }
                    executeResult = "发生错误! 可能原因: ".concat(e.getCommonError().getErrorMsg());
                    break;
                } catch (Exception e) {
                    executeResult = "发生错误! 可能原因: ".concat(e.getMessage());
                    break;
                }
                log.info("key => {}, start => {}", key, start);
                thisc = thisc + 20;
                start = start + 20;
                if (thisc >= JobContanst.DOUBAN_MAX_THUS_PAGE) {
                    log.info("此次收集电影信息已达{},暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}", JobContanst.DOUBAN_MAX_THUS_PAGE, counrty, year1, year2);
                    break;
                }
                if (start >= JobContanst.DOUBAN_MAX_PAGE) {
                    isLock = false;
                    log.info("此段收集电影信息已达{},结束此段任务 ... >>> country: {}, year: {}-{}", JobContanst.DOUBAN_MAX_PAGE, counrty, year1, year2);
                    break;
                }
                TimeUnit.SECONDS.sleep(ChangeRunningContanst.SLEEP_INDEX_SPAN_TIME);
            }
            //修改页数
            updatePageNo(isLock, start, pageNo);

            log.info("豆瓣 {} 条数据加载完毕,即将爬取这些数据的详情页面   >>>", doubanMvMap.size());
            if (picDownUtils.files.size() != 0) {
                Thread t1 = new Thread(picDownUtils);
                exe.execute(t1);
            }
            executeDetail(doubanMvMap, exe);
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    break;
                }
                TimeUnit.SECONDS.sleep(5);
            }
            if (executeResult == null) {
                executeResult = String.format("请求成功,新增电影: %s 条", JobDoubanMvDetail.successCount);
            } else {
                executeResult = String.format("请求成功,新增电影: %s 条, 请求过程中%s", JobDoubanMvDetail.successCount, executeResult);
            }
            JobDoubanMvDetail.successCount = 0;
        }
        log.info(executeResult);
        return executeResult;
    }

    protected <T> void executeDetail(Map<String, T> movies, ExecutorService exe) {
        int size = movies.size() / spList;
        int b = movies.size() % spList;
        log.info("计划创建: {}条任务  >>>", b == 0 ? size : (size + 1));
        log.info("开始执行计划任务项  >>>");
        /**
         * 防止一次任务多个相同id查出后,查询详情插入数据库的主键错误
         */
        List<Mv> mvs = (List<Mv>) movies.values().stream().collect(Collectors.toList());
        for (int i = 0; i < size; i++) {
            List<Mv> m1 = new ArrayList<>(mvs.subList((i * spList), (i + 1) * spList));
            exe.execute(new JobDoubanMvDetail(m1, mvMapper));
        }
        if (b != 0) {
            List<Mv> m1 = new ArrayList<>(mvs.subList(size * spList, movies.size()));
            exe.execute(new JobDoubanMvDetail(m1, mvMapper));
        }
    }

}