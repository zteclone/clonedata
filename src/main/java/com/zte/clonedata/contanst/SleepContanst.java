package com.zte.clonedata.contanst;

import com.zte.clonedata.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ProjectName: clonedata-com.zte.clonedata.contanst
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 14:57 2020/7/15
 * @Description:
 *
 *
 * TODO 数据库配置
 */
@Configuration
public class SleepContanst {

    /**
     * 爬取主页间隔
     */
    public static int SLEEP_INDEX_SPAN_TIME = 90 * 1000;
    /**
     * 主页错误间隔
     */
    public static int SLEEP_INDEX_ERROR_SPAN_TIME = 20 * 1000;

    /**
     * 爬取详情页间隔
     */
    public static int SLEEP_DETAIL_SPAN_TIME = 5 * 1000;
    /**
     * 详情页错误间隔
     */
    public static int SLEEP_DETAIL_ERROR_SPAN_TIME = 5 * 1000;

    /**
     * 线程间隔
     */
    public static int SLEEP_RUN_SPAN_TIME = 2 * 1000;

    /**
     * FTP 间隔
     */
    public static int SLEEP_FTP_SPAN_TIME = 1000;
    @Autowired
    private DictService dictService;
    @PostConstruct
    public void init(){
        int index_span_time = dictService.selectIntByKey("SLEEP_INDEX_SPAN_TIME");
        if (index_span_time!= 0) SLEEP_INDEX_SPAN_TIME = index_span_time * 1000;
        int index_error_span_time = dictService.selectIntByKey("SLEEP_INDEX_ERROR_SPAN_TIME");
        if (index_error_span_time!= 0) SLEEP_INDEX_ERROR_SPAN_TIME = index_error_span_time * 1000;
        int detail_span_time = dictService.selectIntByKey("SLEEP_DETAIL_SPAN_TIME");
        if (detail_span_time!= 0) SLEEP_DETAIL_SPAN_TIME = detail_span_time * 1000;
        int detail_error_span_time = dictService.selectIntByKey("SLEEP_DETAIL_ERROR_SPAN_TIME");
        if (detail_error_span_time!= 0) SLEEP_DETAIL_ERROR_SPAN_TIME = detail_error_span_time * 1000;
        int run_span_time = dictService.selectIntByKey("SLEEP_RUN_SPAN_TIME");
        if (run_span_time!= 0) SLEEP_RUN_SPAN_TIME = run_span_time * 1000;
        int ftp_span_time = dictService.selectIntByKey("SLEEP_FTP_SPAN_TIME");
        if (ftp_span_time!= 0) SLEEP_FTP_SPAN_TIME = ftp_span_time * 1000;
    }
}
