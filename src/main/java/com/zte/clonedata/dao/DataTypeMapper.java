package com.zte.clonedata.dao;

import com.zte.clonedata.model.DataType;
import com.zte.clonedata.model.DataTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 爬取类别
 */
public interface DataTypeMapper {
    long countByExample(DataTypeExample example);

    int deleteByExample(DataTypeExample example);

    int deleteByPrimaryKey(String id);

    int insert(DataType record);

    int insertSelective(DataType record);

    List<DataType> selectByExample(DataTypeExample example);

    DataType selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") DataType record, @Param("example") DataTypeExample example);

    int updateByExample(@Param("record") DataType record, @Param("example") DataTypeExample example);

    int updateByPrimaryKeySelective(DataType record);

    int updateByPrimaryKey(DataType record);

    String getIdByTypeName(String typeName);
}