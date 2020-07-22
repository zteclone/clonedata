package com.zte.clonedata.job.maoyan;

import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.job.utils.HttpThread;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.maoyan
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:38 2020/6/23
 * @Description:
 */
@Component
@Slf4j
public class JobMaoyanMv extends AbstractJob {

    @Autowired
    private DictService dictService;

    public String execute(String counrty, String year1, String year2) throws InterruptedException {
        log.info("猫眼电影开始执行任务   >>>");
        JSONObject countrysJson = dictService.selectJSONByKey("maoyan_countrys_json");
        JSONObject yearsJson = dictService.selectJSONByKey("maoyan_years_json");
        String countryname = (String) countrysJson.get(counrty);
        String yearname = (String) yearsJson.get(year1);
        if (StringUtils.isBlank(countryname) || StringUtils.isBlank(yearname)) {
            return "失败: 参数错误 - 数字";
        }
        String key ="猫眼电影 - ".concat(countryname).concat(yearname);
        log.info("猫眼电影开始执行任务   >>>");
        //检查主目录
        checkBasePath(runningContanst.BASEURL.concat("images").concat(File.separator).concat(Contanst.TYPE_MAOYAN));
        boolean isLock = false;
        String executeResult = null;
        int thisc = 0;
        int addCount = 0;
        synchronized (this) {
            //查询该任务的起始页数
            PageNo pageNo = new PageNo(key, RunningContanst.TYPE_MAOYAN_ID);
            String value = pageNoService.getValueByKeyAndTypeid(pageNo);

            int start = Integer.parseInt(value);

            while (true) {
                String url = String.format(
                        "https://maoyan.com/films?sourceId=%s&yearId=%s&showType=3&offset=%s",
                        counrty, year1, start);
                HttpThread httpThread = new HttpThread(url, Contanst.MAOYAN_HOST1, HttpType.MAOYAN_MV);
                httpThread.join(300000);
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
                thisc = thisc + 30;
                start = start + 30;
                if (thisc >= Contanst.MAOYAN_MAX_THUS_PAGE) {
                    isLock = true;
                    log.info("此次收集电影信息已达{},暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}",Contanst.DOUBAN_MAX_THUS_PAGE, counrty, year1, year2);
                    break;
                }
                if (start >= Contanst.MAOYAN_MAX_PAGE) {
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