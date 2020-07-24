package com.zte.clonedata.job.maoyan;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.PicDownUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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

    public String execute(String counrty, String year1, String year2) throws InterruptedException {
        JSONObject countrysJson = dictService.selectJSONByKey("maoyan_countrys_json");
        JSONObject yearsJson = dictService.selectJSONByKey("maoyan_years_json");
        String countryname = (String) countrysJson.get(counrty);
        String yearname = (String) yearsJson.get(year1);
        if (StringUtils.isBlank(countryname) || StringUtils.isBlank(yearname)) {
            return "失败: 参数错误 - 数字";
        }
        String key ="猫眼电影 - ".concat(countryname).concat(yearname);
        ExecutorService exe = Executors.newCachedThreadPool();
        log.info("{} 开始执行任务   >>>",key);
        Map<String, Mv> maoyanMvMap = Maps.newHashMap();
        PicDownUtils picDownUtils = new PicDownUtils();
        boolean isLock = true;
        String executeResult = null;
        int thisc = 0;
        synchronized (this) {
            //查询该任务的起始页数
            PageNo pageNo = PageNo.builder().key(key).type(RunningContanst.TYPE_MAOYAN_ID).build();
            int start = Integer.parseInt(pageNoService.getValueByKeyAndTypeid(pageNo));
            while (true) {
                String url = String.format(
                        "https://maoyan.com/films?sourceId=%s&yearId=%s&showType=3&offset=%s",
                        counrty, year1, start);
                try {
                    convertStringToObject.getmaoyanMv(url,picDownUtils,maoyanMvMap);
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
                thisc = thisc + 30;
                start = start + 30;
                if (thisc >= JobContanst.MAOYAN_MAX_THUS_PAGE) {
                    log.info("此次收集电影信息已达{},暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}", JobContanst.MAOYAN_MAX_THUS_PAGE, counrty, year1, year2);
                    break;
                }
                if (start >= JobContanst.MAOYAN_MAX_PAGE) {
                    isLock = false;
                    log.info("此段收集电影信息已达{},结束此段任务 ... >>> country: {}, year: {}-{}", JobContanst.MAOYAN_MAX_PAGE, counrty, year1, year2);
                    break;
                }
                Thread.sleep(ChangeRunningContanst.SLEEP_INDEX_SPAN_TIME);
            }
            //修改页数
            updatePageNo(isLock, start, pageNo);

            log.info("猫眼 {} 条数据加载完毕,即将爬取这些数据的详情页面   >>>", maoyanMvMap.size());
            if (picDownUtils.files.size() != 0) {
                Thread t1 = new Thread(picDownUtils);
                exe.execute(t1);
            }
            executeDetail(maoyanMvMap, exe);
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    break;
                }
                Thread.sleep(10000);
            }
            if (executeResult == null) {
                executeResult = String.format("请求成功,新增电影: %s 条", JobMaoyanMvDetail.successCount);
            } else {
                executeResult = String.format("请求成功,新增电影: %s 条, 请求过程中%s", JobMaoyanMvDetail.successCount, executeResult);
            }
            JobMaoyanMvDetail.successCount = 0;
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
        /**
         * 猫眼不做切割执行。 （IP限制包含详情页）
         */
        exe.execute(new JobMaoyanMvDetail(mvs, mvMapper));
    }

}