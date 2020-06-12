package com.zte.clonedata.service.impl;

import com.zte.clonedata.dao.TaskLogMapper;
import com.zte.clonedata.model.TaskLog;
import com.zte.clonedata.model.TaskLogExample;
import com.zte.clonedata.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:32 2019/6/12
 * @Description:
 */
@Service
public class TaskLogServiceImpl implements TaskLogService {

    @Autowired
    private TaskLogMapper taskLogMapper;

    @Override
    public TaskLog selectRunningTaskLogByTaskId(String taskId) {
        TaskLogExample taskLogExample = new TaskLogExample();
        TaskLogExample.Criteria criteria = taskLogExample.createCriteria();
        criteria.andTaskIdEqualTo(taskId);
        criteria.andStatusEqualTo(0);
        List<TaskLog> taskLogs = taskLogMapper.selectByExample(taskLogExample);
        return taskLogs.size() == 0 ? null : taskLogs.get(0);
    }

    @Override
    public void addTaskLog(TaskLog taskLog) {
        taskLogMapper.insertSelective(taskLog);
    }

    @Override
    public void updateTaskLog(TaskLog taskLog) {
        taskLogMapper.updateByPrimaryKeySelective(taskLog);
    }
}
