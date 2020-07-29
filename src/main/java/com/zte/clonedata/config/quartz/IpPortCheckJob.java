package com.zte.clonedata.config.quartz;

import com.zte.clonedata.dao.IpProxyMapper;
import com.zte.clonedata.util.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * ProjectName: clonedata-com.zte.clonedata.config.quartz
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:28 2020/7/17
 * @Description:
 */
public class IpPortCheckJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        IpProxyMapper ipProxyMapper = SpringContextUtil.getBean(IpProxyMapper.class);
        ipProxyMapper.deleteError();
        ipProxyMapper.updateOld();
    }
}
