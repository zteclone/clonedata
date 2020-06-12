package com.zte.clonedata.dao;

import com.zte.clonedata.model.Tv;
import com.zte.clonedata.model.TvExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TvMapper {
    long countByExample(TvExample example);

    int deleteByExample(TvExample example);

    int deleteByPrimaryKey(String id);

    int insert(Tv record);

    int insertSelective(Tv record);

    List<Tv> selectByExample(TvExample example);

    Tv selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Tv record, @Param("example") TvExample example);

    int updateByExample(@Param("record") Tv record, @Param("example") TvExample example);

    int updateByPrimaryKeySelective(Tv record);

    int updateByPrimaryKey(Tv record);
}