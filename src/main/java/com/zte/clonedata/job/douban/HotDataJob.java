package com.zte.clonedata.job.douban;

import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.job.utils.HttpThread;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.service.PageNoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.douban
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:47 2020/7/27
 * @Description:
 *
 * 豆瓣热点数据执行类
 */
@Slf4j
@Component
public class HotDataJob implements Job {

    @Autowired
    protected PageNoService pageNoService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String[] programTypes = {"电影", "电视剧"};
        String[] types = {"U", "T", "S", "R"};
        Random random = new Random();
        int pr = random.nextInt(programTypes.length);
        int tr = random.nextInt(types.length);
        String programType = programTypes[pr];
        String type = types[tr];
        HttpType httpType = null;
        int successCnt = 0;
        for (int i = 0; i <= 40; i = i + 20) {
            String url = String.format("https://movie.douban.com/j/new_search_subjects?sort=%s&range=0,10&tags=%s&start=%s", type, programType, i);
            try {
                if (pr == 0) {
                    httpType = HttpType.DOUBAN_MY;
                } else if (pr == 1) {
                    httpType = HttpType.DOUBAN_TY;
                }
                HttpThread thread = new HttpThread(url, httpType);
                thread.join();
                successCnt += thread.getAddCount();
                log.info("热门{}，start：{}，类型：{}",programType,i,type);
                TimeUnit.SECONDS.sleep(ChangeRunningContanst.SLEEP_INDEX_SPAN_TIME);
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }

        log.info("本次获取热点数据任务新增 {}：{}条",programType,successCnt);
    }
}
