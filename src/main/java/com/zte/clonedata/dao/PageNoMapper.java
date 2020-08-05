package com.zte.clonedata.dao;

import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.model.PageNoExample;
import java.util.List;

import com.zte.clonedata.web.dto.PageNoDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 页数
 */
@Repository
public interface PageNoMapper {
    long countByExample(PageNoExample example);

    int deleteByExample(PageNoExample example);

    int deleteByPrimaryKey(String id);

    int insert(PageNo record);

    int insertSelective(PageNo record);

    List<PageNo> selectByExample(PageNoExample example);

    PageNo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") PageNo record, @Param("example") PageNoExample example);

    int updateByExample(@Param("record") PageNo record, @Param("example") PageNoExample example);

    int updateByPrimaryKeySelective(PageNo record);

    int updateByPrimaryKey(PageNo record);

    String getValueByKeyAndTypeid(PageNo record);

    int updateValueByKeyAndTypeid(PageNo record);
    int updateValueByIdAndTypeid(PageNo record);

    List<PageNoDTO> selectList(PageNoDTO pageNoDTO);
}