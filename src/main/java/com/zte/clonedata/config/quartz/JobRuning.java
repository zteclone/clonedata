package com.zte.clonedata.config.quartz;

import com.zte.clonedata.model.TaskLog;
import com.zte.clonedata.service.ExecuteService;
import com.zte.clonedata.service.TaskLogService;
import com.zte.clonedata.util.ScheduleUtils;
import com.zte.clonedata.util.SpringContextUtil;
import com.zte.clonedata.util.UUIDUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:31 2019/6/12
 * @Description:
 */

public class JobRuning implements Job {
    private static final Logger log = LoggerFactory.getLogger(JobRuning.class);

    private static TaskLogService taskLogService;

    private static ExecuteService executeService;

    public JobRuning() {
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        String id = UUIDUtils.get();
        if (taskLogService == null){
            taskLogService = SpringContextUtil.getBean("taskLogServiceImpl");
            executeService = SpringContextUtil.getBean("executeService");
        }
        try {
            String taskId = (String)mergedJobDataMap.get(ScheduleUtils.getJOB_PARAM_KEY());
            String lid = (String)mergedJobDataMap.get(ScheduleUtils.getLID());
            log.debug("任务id:" + taskId);
            TaskLog taskLog = taskLogService.selectRunningTaskLogByTaskId(taskId);
            if (taskLog == null) {
                executeService.executeJob(id, taskId, (List)null, "0", lid);
            } else {
                log.debug("任务id: " + taskId + " 正在运行中，执行完毕后才能继续执行任务");
            }
        } catch (Exception var9) {
            log.error("自动执行异常：" + var9.getMessage());
        }

    }
}