package com.zte.clonedata.dao;

import com.zte.clonedata.model.TaskParam;
import com.zte.clonedata.model.TaskParamExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskParamMapper {
    long countByExample(TaskParamExample example);

    int deleteByExample(TaskParamExample example);

    int deleteByPrimaryKey(String id);

    int insert(TaskParam record);

    int insertSelective(TaskParam record);

    List<TaskParam> selectByExample(TaskParamExample example);

    TaskParam selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TaskParam record, @Param("example") TaskParamExample example);

    int updateByExample(@Param("record") TaskParam record, @Param("example") TaskParamExample example);

    int updateByPrimaryKeySelective(TaskParam record);

    int updateByPrimaryKey(TaskParam record);
}