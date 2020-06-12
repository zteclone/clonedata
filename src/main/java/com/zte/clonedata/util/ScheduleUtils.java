package com.zte.clonedata.util;

import com.zte.clonedata.config.quartz.JobRuning;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:31 2019/6/12
 * @Description:
 */

public class ScheduleUtils {
    private static final Logger log = LoggerFactory.getLogger(ScheduleUtils.class);
    private static Scheduler scheduler = null;
    private static final String JOB_GROUP_NAME = "myGroup";
    public static final String JOB_PARAM_KEY = "taskId";
    public static final String LID = "lid";

    public ScheduleUtils() {
    }

    public static Scheduler getScheduler() {
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }

    public static void addScheduleJob(String taskId, String cron, String lid) throws SchedulerException {
        addScheduleJob(taskId, JOB_GROUP_NAME, cron, taskId, lid);
    }

    public static void addScheduleJob(String jobName, String jobGroup, String cronExpression, Object param, String lid) throws SchedulerException {
        Class<? extends Job> jobClass = JobRuning.class;
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
        jobDetail.getJobDataMap().put(JOB_PARAM_KEY, param);
        jobDetail.getJobDataMap().put(LID, lid);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

    public static void updateScheduleJob(String taskId, String cronExpression, String lid) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(taskId, JOB_GROUP_NAME);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            addScheduleJob(taskId, cronExpression, lid);
        } else {
            trigger = (CronTrigger)trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    public static void deleteScheduleJob(String taskId) throws SchedulerException {
        scheduler.deleteJob(getJobKey(taskId, JOB_GROUP_NAME));
    }

    public static void updateDelayJobTime(String taskId, int second, String lid) throws SchedulerException {
        String date = "";
        log.debug("间隔:{}秒后的时间为:{}", second, date);
        String cronExpression = CronUtils.getCron(new Date());
        updateScheduleJob(taskId, cronExpression, lid);
    }

    public static void runOnce(String taskId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(taskId, JOB_GROUP_NAME);
        scheduler.triggerJob(jobKey);
    }

    public static void pauseJob(String taskId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(taskId, JOB_GROUP_NAME);
        scheduler.pauseJob(jobKey);
    }

    public static void resumeJob(String taskId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(taskId, JOB_GROUP_NAME);
        scheduler.resumeJob(jobKey);
    }

    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    public static JobKey getJobKey(String jobName, String jobGroup) {
        return JobKey.jobKey(jobName, jobGroup);
    }

    public static String getJOB_PARAM_KEY() {
        return JOB_PARAM_KEY;
    }

    public static String getLID() {
        return LID;
    }

    static {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            List<String> jobGroupNames = scheduler.getJobGroupNames();
        } catch (SchedulerException var1) {
            log.error(var1.getMessage());
        }

    }
}