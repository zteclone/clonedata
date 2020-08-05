package com.zte.clonedata.dao;

import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.TaskManagementExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagementMapper {
    long countByExample(TaskManagementExample example);

    int deleteByExample(TaskManagementExample example);

    int deleteByPrimaryKey(String id);

    int insert(TaskManagement record);

    int insertSelective(TaskManagement record);

    List<TaskManagement> selectByExample(TaskManagementExample example);

    TaskManagement selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TaskManagement record, @Param("example") TaskManagementExample example);

    int updateByExample(@Param("record") TaskManagement record, @Param("example") TaskManagementExample example);

    int updateByPrimaryKeySelective(TaskManagement record);

    int updateByPrimaryKey(TaskManagement record);
}