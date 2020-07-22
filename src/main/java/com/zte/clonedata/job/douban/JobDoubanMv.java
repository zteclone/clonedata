package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.job.utils.HttpThread;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
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
public class JobDoubanMv extends AbstractJob {

    public String execute(String counrty, String year1, String year2) throws InterruptedException {
        log.info("豆瓣电影开始执行任务   >>>");
        //检查主目录
        checkBasePath(runningContanst.BASEURL.concat("images").concat(File.separator).concat(Contanst.TYPE_DOUBAN));
        boolean isLock = true;
        String executeResult = null;
        int thisc = 0;
        int addCount = 0;
        String key = "豆瓣电影 - ".concat(counrty).concat(year1).concat(year2);
        synchronized (this) {
            //查询该任务的起始页数
            PageNo pageNo = new PageNo(key, RunningContanst.TYPE_DOUBAN_ID);
            int start = Integer.parseInt(pageNoService.getValueByKeyAndTypeid(pageNo));
            while (true) {
                String url = String.format(
                        "https://movie.douban.com/j/new_search_subjects?sort=U&range=0,10&tags=电影&countries=%s&year_range=%s,%s&start=%s",
                        counrty, year1, year2, start);
                HttpThread httpThread = new HttpThread(url, Contanst.DOUBAN_HOST1, HttpType.DOUBAN_MY);
                httpThread.join(150000);
                if (!httpThread.isAlive()) {
                    addCount += httpThread.getAddCount();
                    String errMsg = httpThread.getErrMsg();
                    if (StringUtils.isNotBlank(errMsg)) {
                        executeResult = "发生错误! 可能原因: ".concat(errMsg);
                        break;
                    }
                    if (! httpThread.isPageLock()) {
                        isLock = false;
                        break;
                    }
                }else {
                    executeResult = "发生错误! 可能原因: http请求超时,切断连接";
                }

                log.info("key => {}, start => {}", key, start);
                thisc = thisc + 20;
                start = start + 20;
                if (thisc >= Contanst.DOUBAN_MAX_THUS_PAGE) {
                    log.info("此次收集电影信息已达{},暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}",Contanst.DOUBAN_MAX_THUS_PAGE, counrty, year1, year2);
                    break;
                }
                if (start >= Contanst.DOUBAN_MAX_PAGE) {
                    isLock = false;
                    log.info("此段收集电影信息已达{},结束此段任务 ... >>> country: {}, year: {}-{}", Contanst.MAOYAN_MAX_PAGE, counrty, year1, year2);
                    break;
                }
                Thread.sleep(ChangeRunningContanst.SLEEP_INDEX_SPAN_TIME);
            }
            //修改页数
            updatePageNo(isLock, start, pageNo);
        }
        if (executeResult == null) {
            executeResult = String.format("请求成功,新增电影: %s 条", addCount);
        } else {
            executeResult = String.format("请求成功,新增电影: %s 条, 请求过程中%s", addCount, executeResult);
        }
        log.info(executeResult);
        return executeResult;
    }
}