package com.zte.clonedata.dao;

import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.MvExample;

import java.util.List;
import java.util.Map;

import com.zte.clonedata.web.dto.MvDTO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 电影表
 */
@Repository
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

    String getNumByPrimaryKey(@Param("movieid") String movieid,@Param("mvTypeid")String mvTypeid);

    void updateRatingByPrimarKey(@Param("ratingnum") String ratingnum,
                                 @Param("movieid") String movieid,
                                 @Param("mvTypeid")String mvTypeid);
    @MapKey("d")
    Map<String, Map<String,Object>> getMvSevenCnt();
}