package com.zte.clonedata.dao;

import com.zte.clonedata.model.DoubanMv;
import com.zte.clonedata.model.DoubanMvExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DoubanMvMapper {
    long countByExample(DoubanMvExample example);

    int deleteByExample(DoubanMvExample example);

    int deleteByPrimaryKey(String movieid);

    int insert(DoubanMv record);

    int insertSelective(DoubanMv record);

    List<DoubanMv> selectByExample(DoubanMvExample example);

    DoubanMv selectByPrimaryKey(String movieid);

    int updateByExampleSelective(@Param("record") DoubanMv record, @Param("example") DoubanMvExample example);

    int updateByExample(@Param("record") DoubanMv record, @Param("example") DoubanMvExample example);

    int updateByPrimaryKeySelective(DoubanMv record);

    int updateByPrimaryKey(DoubanMv record);
}