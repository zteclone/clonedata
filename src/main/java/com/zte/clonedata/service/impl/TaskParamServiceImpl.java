package com.zte.clonedata.service.impl;

import com.zte.clonedata.dao.TaskParamMapper;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.TaskParam;
import com.zte.clonedata.model.TaskParamExample;
import com.zte.clonedata.service.TaskParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 15:24 2019/6/13
 * @Description:
 */
@Service
public class TaskParamServiceImpl implements TaskParamService {

    @Autowired
    private TaskParamMapper taskParamMapper;


    @Override
    public List<TaskParam> selectTaskParamListByTaskManagement(TaskManagement taskManagement) {
        if (StringUtils.isBlank(taskManagement.getTaskId())) {
            throw new RuntimeException("id不允许为空");
        } else {
            TaskParamExample example = new TaskParamExample();
            TaskParamExample.Criteria criteria = example.createCriteria();
            criteria.andTaskIdEqualTo(taskManagement.getTaskId());
            return taskParamMapper.selectByExample(example);
        }
    }
}
