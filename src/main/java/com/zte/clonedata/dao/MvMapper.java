package com.zte.clonedata.dao;

import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.MvExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MvMapper {
    long countByExample(MvExample example);

    int deleteByExample(MvExample example);

    int deleteByPrimaryKey(String id);

    int insert(Mv record);

    int insertSelective(Mv record);

    List<Mv> selectByExample(MvExample example);

    Mv selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Mv record, @Param("example") MvExample example);

    int updateByExample(@Param("record") Mv record, @Param("example") MvExample example);

    int updateByPrimaryKeySelective(Mv record);

    int updateByPrimaryKey(Mv record);
}