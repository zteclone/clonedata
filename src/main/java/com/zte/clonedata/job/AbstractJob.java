package com.zte.clonedata.job;

import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.utils.ConvertStringToObject;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.service.PageNoService;
import com.zte.clonedata.util.FileUtils;
import com.zte.clonedata.util.PicDownUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 14:55 2020/6/23
 * @Description:
 */
public abstract class AbstractJob {
    @Autowired
    protected MvMapper mvMapper;
    @Autowired
    protected RunningContanst runningContanst;
    @Autowired
    protected PageNoService pageNoService;
    @Autowired
    protected ConvertStringToObject convertStringToObject;

    /**
     * 任务线程主类
     * @param counrty 国家
     * @param year1 起始年份
     * @param year2 终止年份
     */
    protected abstract String execute(String counrty,String year1,String year2) throws InterruptedException;

    /**
     * 校验主目录
     */
    protected void checkBasePath(String path) {
        FileUtils.existsMkdir(path);
    }
    protected void updatePageNo(boolean isLock, Integer start, PageNo pageNo) {
        if (isLock) {
            pageNo.setValue(String.valueOf(start));
        } else {
            pageNo.setValue("0");
        }
        pageNoService.updateValueByKeyAndTypeid(pageNo);
    }
}