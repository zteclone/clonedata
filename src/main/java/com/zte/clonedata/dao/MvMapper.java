package com.zte.clonedata.dao;

import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.MvExample;

import java.util.List;

import com.zte.clonedata.web.dto.MvDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 电影表
 */
public interface MvMapper {
    long countByExample(MvExample example);

    int deleteByExample(MvExample example);

    int deleteByPrimaryKey(Mv mv);

    int insert(Mv mv);

    int insertSelective(Mv mv);

    List<Mv> selectByExample(MvExample example);

    Mv selectByPrimaryKey(Mv mv);

    int updateByExampleSelective(@Param("record") Mv mv , @Param("example") MvExample example);

    int updateByExample(@Param("record") Mv record, @Param("example") MvExample example);

    int updateByPrimaryKeySelective(Mv mv);

    int updateByPrimaryKey(Mv record);

    MvDTO selectTypenameByMovieidAndMvtypeid(Mv mv);
}