package com.zte.clonedata.web;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zte.clonedata.util.ResponseUtils;
import com.zte.clonedata.util.ScheduleUtils;
import com.zte.clonedata.web.dto.JobAndTriggerDTO;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.web.bind.annotation.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 15:25 2020/6/18
 * @Description:
 *
 * quartz Web页面  >>>         http://localhost:8090/admin/quartz
 *
 * 暂时无用
 */
@RestController
@RequestMapping(value="/quartz")
public class QuartzWeb {


    @PostMapping(value="/addjob")
    public void addjob(@RequestParam(value="jobClassName")String jobClassName,
                       @RequestParam(value="jobGroupName")String jobGroupName,
                       @RequestParam(value="cronExpression")String cronExpression) throws Exception {
        ScheduleUtils.addScheduleJob(jobClassName,cronExpression,"");
    }

    @PostMapping(value="/pausejob")
    public void pausejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        ScheduleUtils.pauseJob(jobClassName);
    }


    @PostMapping(value="/resumejob")
    public void resumejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        ScheduleUtils.resumeJob(jobClassName);
    }


    @PostMapping(value="/reschedulejob")
    public void rescheduleJob(@RequestParam(value="jobClassName")String jobClassName,
                              @RequestParam(value="jobGroupName")String jobGroupName,
                              @RequestParam(value="cronExpression")String cronExpression) throws Exception {
        ScheduleUtils.updateScheduleJob(jobClassName,cronExpression,"");
    }



    @PostMapping(value="/deletejob")
    public void deletejob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        ScheduleUtils.deleteScheduleJob(jobClassName);
    }


    private static final Map<String,String> stateMap;
    static {
        stateMap = Maps.newHashMap();
        stateMap.put("NORMAL","正常");
        stateMap.put("PAUSED","暂停");
        stateMap.put("COMPLETE","完成");
        stateMap.put("ERROR","错误");
        stateMap.put("BLOCKED","阻塞");
    }

    @GetMapping(value="/queryjob")
    public ResponseUtils queryjob(@RequestParam(value="pageNum")Integer pageNum, @RequestParam(value="pageSize")Integer pageSize) {
        List<JobAndTriggerDTO> list = Lists.newArrayList();
        try {
            Set<JobKey> jobKeys = ScheduleUtils.getScheduler().getJobKeys(GroupMatcher.anyGroup());
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggersOfJob = ScheduleUtils.getScheduler().getTriggersOfJob(jobKey);
                JobAndTriggerDTO dto = new JobAndTriggerDTO();
                for (Trigger trigger : triggersOfJob) {
                    dto.setJOB_NAME(jobKey.getName());
                    dto.setJOB_GROUP(jobKey.getGroup());
                    dto.setTRIGGER_NAME(((CronTriggerImpl) trigger).getFullName());
                    Trigger.TriggerState triggerState = ScheduleUtils.getScheduler().getTriggerState(trigger.getKey());
                    dto.setJOB_STATUS(stateMap.get(triggerState.name()));
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        dto.setCRON_EXPRESSION(cronExpression);
                    }
                }
                list.add(dto);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return ResponseUtils.success(list.size(),list);
    }

}
