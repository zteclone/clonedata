package com.zte.clonedata.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zte.clonedata.model.TaskLog;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.TaskParam;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.HttpUtils;
import com.zte.clonedata.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Liangxiaomin
 * @Date Created in 15:20 2019/6/13
 * @Description:
 */
@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class ExecuteService {

    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private TaskManagementService taskManagementService;
    @Autowired
    private TaskParamService taskParamService;

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED
    )
    public void executeJob(String id, String taskId, List<TaskParam> taskParamList, String type, String lid) throws Exception {
        long start = System.currentTimeMillis();
        TaskManagement taskManagement = this.taskManagementService.selectTaskManagementByTaskId(taskId);
        if (taskParamList == null) {
            taskParamList = this.taskParamService.selectTaskParamListByTaskManagement(taskManagement);
        }

        if (taskManagement != null) {
            TaskLog taskLog = new TaskLog();
            taskLog.setId(id);
            taskLog.setTaskId(taskManagement.getTaskId());
            //taskLog.setTaskName(taskManagement.getTaskName());
            taskLog.setBegintime(DateUtils.getNowYYYYMMDDHHMMSS());
            taskLog.setStatus(0);
            this.taskLogService.addTaskLog(taskLog);
            Map<String, String> resultMap = Maps.newHashMap();
            if ("http-get".equals(taskManagement.getTaskType())) {
                resultMap = HttpUtils.doGet(taskManagement.getExcuteTarget(), this.toParamMap(taskParamList), taskManagement.getTimeoutSecond() == null ? null : taskManagement.getTimeoutSecond() * 1000, lid);
                this.setResult((Map)resultMap, taskLog);
            }

            taskLog.setTime(System.currentTimeMillis()-start);
            taskLog.setEndtime(DateUtils.getNowYYYYMMDDHHMMSS());
            this.taskLogService.updateTaskLog(taskLog);
        }

    }

    private Map<String, String> toParamMap(List<TaskParam> taskParamList) {
        Map<String, String> map = new HashMap(16);
        if (taskParamList != null) {
            taskParamList.forEach((x) -> {
                map.put(x.getParamName(), x.getParamValue());
            });
        }

        return map;
    }

    private void setResult(Map<String, String> msg, TaskLog taskLog) {
        String status = msg.get("STATUS");
        String message = msg.get("MESSAGE");
        ResponseUtils responseUtils = JSONObject.parseObject(message, ResponseUtils.class);
        taskLog.setExecuteResult(responseUtils.getMsg());
        if ("SUCCESS".equals(status) && responseUtils.getCode().equals("0")) {
            taskLog.setStatus(1);
        } else {
            taskLog.setStatus(2);
        }


    }



}
