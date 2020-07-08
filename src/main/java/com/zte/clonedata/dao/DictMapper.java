package com.zte.clonedata.dao;

import com.zte.clonedata.model.Dict;

import java.util.List;

public interface DictMapper {

    int deleteByPrimaryKey(String id);

    int insert(Dict record);

    Dict selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Dict record);

    String selectByKey(String key);

    List<Dict> selectList(Dict dict);
}