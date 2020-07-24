package com.zte.clonedata.service;

import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.error.BusinessException;

import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:26 2019/6/12
 * @Description:
 */
public interface TaskManagementService {
    List<TaskManagement> selectTaskManagementList();
    List<TaskManagement> select(String taskname);

    void updateByPrimaryKeySelective(TaskManagement x);

    TaskManagement selectTaskManagementByTaskId(String taskId) throws BusinessException;

    void insert(TaskManagement taskManagement);

    void delById(String id);

    TaskManagement selectTaskManagementByTaskname(String taskName) throws BusinessException;

}
