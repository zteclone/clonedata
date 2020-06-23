package com.zte.clonedata.service.impl;

import com.zte.clonedata.dao.TaskManagementMapper;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.TaskManagementExample;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.service.TaskManagementService;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:26 2019/6/12
 * @Description:
 */
@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    @Autowired
    private TaskManagementMapper taskManagementMapper;

    @Override
    public List<TaskManagement> selectTaskManagementList() {
        TaskManagementExample example = new TaskManagementExample();
        TaskManagementExample.Criteria criteria = example.createCriteria();
        criteria.andTaskStatusEqualTo("1");
        List<TaskManagement> taskManagements = taskManagementMapper.selectByExample(example);
        return taskManagements;
    }

    @Override
    public List<TaskManagement> select(String taskname) {
        TaskManagementExample example = new TaskManagementExample();
        example.setOrderByClause("cre_dt+0 asc");
        TaskManagementExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(taskname)){
            criteria.andTaskNameLike("%"+taskname+"%");
        }
        return taskManagementMapper.selectByExample(example);
    }

    @Override
    public void updateByPrimaryKeySelective(TaskManagement x) {
        taskManagementMapper.updateByPrimaryKeySelective(x);
    }

    @Override
    public TaskManagement selectTaskManagementByTaskId(String taskId) throws BusinessException {
        if (StringUtils.isBlank(taskId)) {
            throw new BusinessException(EmBusinessError.JOB_ID_IS_NULL);
        } else {
            TaskManagementExample example = new TaskManagementExample();
            TaskManagementExample.Criteria criteria = example.createCriteria();
            criteria.andTaskIdEqualTo(taskId);
            List<TaskManagement> list = taskManagementMapper.selectByExample(example);
            return list.size()==0?null:list.get(0);
        }
    }

    @Override
    public void insert(TaskManagement taskManagement) {
        String id = UUIDUtils.get();
        taskManagement.setTaskStatus("1");
        taskManagement.setTimeoutSecond((short) 30000);
        taskManagement.setId(id);
        taskManagement.setTaskId(id);
        taskManagement.setCreDt(DateUtils.getNowYYYYMMDDHHMMSS());
        taskManagementMapper.insertSelective(taskManagement);
    }

    @Override
    public void delById(String id) {
        taskManagementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TaskManagement selectTaskManagementByTaskname(String taskName) throws BusinessException {
        if (StringUtils.isBlank(taskName)) {
            throw new BusinessException(EmBusinessError.JOB_NAME_IS_NULL);
        } else {
            TaskManagementExample example = new TaskManagementExample();
            TaskManagementExample.Criteria criteria = example.createCriteria();
            criteria.andTaskNameEqualTo(taskName);
            List<TaskManagement> list = taskManagementMapper.selectByExample(example);
            return list.size()==0?null:list.get(0);
        }
    }

    @Override
    @Transactional()
    public void updateExecuteWeek(String week) {
        TaskManagementExample example = new TaskManagementExample();
        TaskManagementExample.Criteria criteria = example.createCriteria();
        criteria.andTaskNameLike("豆瓣  电影  %");
        List<TaskManagement> list = taskManagementMapper.selectByExample(example);
        list.forEach(x -> {
            TaskManagement taskManagement = new TaskManagement();
            taskManagement.setId(x.getId());
            String taskExcutePlan = x.getTaskExcutePlan();
            taskManagement.setTaskExcutePlan(taskExcutePlan.substring(0,taskExcutePlan.length() - 1).concat(week));
            taskManagementMapper.updateByPrimaryKeySelective(taskManagement);
        });
    }
}
