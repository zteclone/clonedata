package com.zte.clonedata.service;

import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.model.Dict;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.service
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 15:25 2020/7/3
 * @Description:
 */
public interface DictService {

    String selectByKey(String key);

    int selectIntByKey(String key);

    JSONObject selectJSONByKey(String key);

    void insert(String key,String value,String desc);

    List<Dict> select(String key);

    void update(Dict dict);
}
