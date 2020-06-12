package com.zte.clonedata.service;

import com.zte.clonedata.model.TaskLog;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:32 2019/6/12
 * @Description:
 */
public interface TaskLogService {
    TaskLog selectRunningTaskLogByTaskId(String taskId);

    void addTaskLog(TaskLog taskLog);

    void updateTaskLog(TaskLog taskLog);
}
