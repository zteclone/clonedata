package com.zte.clonedata.service;

import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.TaskParam;

import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 15:24 2019/6/13
 * @Description:
 */
public interface TaskParamService {
    List<TaskParam> selectTaskParamListByTaskManagement(TaskManagement taskManagement);
}
