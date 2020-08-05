package com.zte.clonedata.dao;

import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.DoubanTvExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoubanTvMapper {
    long countByExample(DoubanTvExample example);

    int deleteByExample(DoubanTvExample example);

    int deleteByPrimaryKey(String tvid);

    int insert(DoubanTv record);

    int insertSelective(DoubanTv record);

    List<DoubanTv> selectByExample(DoubanTvExample example);

    DoubanTv selectByPrimaryKey(String tvid);

    int updateByExampleSelective(@Param("record") DoubanTv record, @Param("example") DoubanTvExample example);

    int updateByExample(@Param("record") DoubanTv record, @Param("example") DoubanTvExample example);

    int updateByPrimaryKeySelective(DoubanTv record);

    int updateByPrimaryKey(DoubanTv record);

    @MapKey("d")
    Map<String, Map<String,Object>> getTvSevenCnt();
}