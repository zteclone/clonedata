package com.zte.clonedata.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zte.clonedata.config.quartz.InitTaskListener;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.TaskParam;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.service.TaskManagementService;
import com.zte.clonedata.service.TaskParamService;
import com.zte.clonedata.util.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.web.dto
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:58 2020/6/11
 * @Description:
 */
@RestController
@RequestMapping("/jobWeb")
@Slf4j
public class JobWeb {

    @Autowired
    private TaskManagementService taskManagementService;

    /**
     * 新增任务
     * @param taskManagement id,name,cron....
     * @return
     */
    @PostMapping(path = "/add")
    public ResponseUtils add(@RequestBody TaskManagement taskManagement) {
        try {
            if (!CronUtils.isValidExpression(taskManagement.getTaskExcutePlan())){
                return ResponseUtils.fail("新增任务失败: cron表达式错误");
            }
            TaskManagement t2 = taskManagementService.selectTaskManagementByTaskname(taskManagement.getTaskName());
            if (t2 != null){
                return ResponseUtils.fail("新增任务失败: 存在相同的任务名称");
            }
            taskManagementService.insert(taskManagement);
        } catch (ParseException e) {
            return ResponseUtils.fail("新增任务失败: ".concat(e.getMessage()));
        } catch (BusinessException e) {
            return ResponseUtils.fail("新增任务失败: ".concat(e.getCommonError().getErrorMsg()));
        }
        return ResponseUtils.success("新增任务成功");
    }

    /**
     * 删除任务
     * @param id 任务id
     * @return
     */
    @GetMapping(path = "/del")
    public ResponseUtils del(@RequestParam("id")String id) {
        taskManagementService.delById(id);
        try {
            ScheduleUtils.deleteScheduleJob(id);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return ResponseUtils.success("删除成功");
    }

    /**
     * 任务执行一次
     * @param id 任务id
     * @return
     */
    @GetMapping(path = "/execute")
    public ResponseUtils execute(@RequestParam("id")String id) {
        try {
            ScheduleUtils.runOnce(id,ScheduleUtils.JOB_GROUP_NAME);
            return ResponseUtils.success("执行成功，稍后请在任务详情处查看。");
        } catch (SchedulerException e) {
            return ResponseUtils.success("执行失败: 未装载定时任务");
        }
    }

    /**
     * 装载任务
     * @param id 任务id
     * @return
     */
    @GetMapping(path = "/start")
    public ResponseUtils start(@RequestParam("id")String id){
        try {
            TaskManagement x = taskManagementService.selectTaskManagementByTaskId(id);
            if (!StringUtils.isEmpty(x.getTaskExcutePlan()) && CronUtils.isValidExpression(x.getTaskExcutePlan())) {
                log.info("> 定时任务:" + x.getTaskName() + "==== 正在装载...........");
                ScheduleUtils.addScheduleJob(x.getTaskId(), x.getTaskExcutePlan(), x.getExternalCode());
            } else {
                x.setTaskStatus("2");
                this.taskManagementService.updateByPrimaryKeySelective(x);
                return ResponseUtils.fail("装载失败: 校验表达式" + CronUtils.isValidExpression(x.getTaskExcutePlan()));
            }
        } catch (Exception var3) {
            log.error("> 装载定时任务失败", var3);
            return ResponseUtils.success("装载失败: ".concat(var3.getMessage()));
        }
        return ResponseUtils.success("装载成功");
    }

    /**
     * 卸载任务
     * @param id 任务id
     * @return
     */
    @GetMapping(path = "/stop")
    public ResponseUtils stop(@RequestParam("id")String id){
        try {
            ScheduleUtils.deleteScheduleJob(id);
        } catch (Exception var3) {
            log.error("> 卸载定时任务失败", var3);
            return ResponseUtils.success("卸载失败: ".concat(var3.getMessage()));
        }
        return ResponseUtils.success("任务卸载成功");
    }

    /**
     * 修改任务
     * @param taskManagement id,name,cron...
     * @return
     */
    @PostMapping(path = "/update")
    public ResponseUtils update(@RequestBody TaskManagement taskManagement){
        try {
            if (!CronUtils.isValidExpression(taskManagement.getTaskExcutePlan())){
                return ResponseUtils.fail("修改任务失败: cron表达式错误");
            }
            TaskManagement t2 = taskManagementService.selectTaskManagementByTaskname(taskManagement.getTaskName());
            if (t2 != null && !taskManagement.getId().equals(t2.getId())){
                return ResponseUtils.fail("修改任务失败: 存在相同的任务名称");
            }
            taskManagement.setTaskStatus("1");
            taskManagementService.updateByPrimaryKeySelective(taskManagement);
        } catch (ParseException var3) {
            log.error("修改失败: {}", var3.getMessage());
            return ResponseUtils.success("修改失败: ".concat(var3.getMessage()));
        } catch (BusinessException e) {
            log.error("修改失败: {}", e.getCommonError().getErrorMsg());
            return ResponseUtils.success("修改失败: ".concat(e.getCommonError().getErrorMsg()));
        }
        return ResponseUtils.success("修改成功");
    }
}
