package com.zte.clonedata.job;

import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.utils.JobConvertStringToObject;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.service.PageNoService;
import com.zte.clonedata.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 14:55 2020/6/23
 * @Description:
 */
@Slf4j
public abstract class AbstractJob {
    @Autowired
    protected MvMapper mvMapper;
    @Autowired
    protected PageNoService pageNoService;
    @Autowired
    protected ChangeRunningContanst changeRunningContanst;
    @Autowired
    protected RunningContanst runningContanst;
    @Autowired
    protected JobConvertStringToObject convertStringToObject;
    @Autowired
    protected DoubanTvMapper doubanTvMapper;
    @Autowired
    protected DictService dictService;
    /**
     * 切割集合并发访问因子
     */
    protected static final int spList = 20;

    /**
     * 任务线程主类
     *
     * @param counrty 国家
     * @param year1   起始年份
     * @param year2   终止年份
     */
    protected abstract String execute(String counrty, String year1, String year2) throws InterruptedException;

    /**
     * 执行详单访问保存操作
     *
     * @param dataMap 需要访问详单的数据集合
     * @param exe     多线程执行类
     * @param <T>     根据网站类型的实体类
     */
    protected abstract <T> void executeDetail(Map<String, T> dataMap, ExecutorService exe);

    /**
     * 校验主目录
     */
    @PostConstruct
    public void init() {
        String[] paths = {runningContanst.BASEURL.concat(JobContanst.TYPE_DOUBAN)
                , runningContanst.BASEURL.concat(JobContanst.TYPE_MAOYAN)
        };
        for (String path : paths) {
            FileUtils.existsMkdir(path);
        }
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