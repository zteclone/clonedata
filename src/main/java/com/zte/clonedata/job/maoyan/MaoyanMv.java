package com.zte.clonedata.job.maoyan;

import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.PicDownUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.maoyan
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:38 2020/6/23
 * @Description:
 */
@Component
@Slf4j
public class MaoyanMv extends AbstractJob {


    @Override
    protected String execute(String counrty, String year1, String year2) throws InterruptedException {
        return null;
    }

    @Override
    protected <T> void getListByURL(String url, PicDownUtils picDownUtils, Map<String, T> dataMap) throws InterruptedException, BusinessException {

    }

    @Override
    protected <T> void executeDetail(Map<String, T> dataMap, ExecutorService exe) {

    }
}
