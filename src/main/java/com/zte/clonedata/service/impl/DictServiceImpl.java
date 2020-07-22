package com.zte.clonedata.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.dao.DictMapper;
import com.zte.clonedata.model.Dict;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.service.impl
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 15:26 2020/7/3
 * @Description:
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private ChangeRunningContanst changeRunningContanst;

    @Override
    public String selectByKey(String key) {
        String value = dictMapper.selectByKey(key);
        if (StringUtils.isNotBlank(value)){
            return value;
        }
        return "";
    }

    @Override
    public int selectIntByKey(String key) {
        String value = dictMapper.selectByKey(key);
        if (StringUtils.isNotBlank(value)){
            return Integer.parseInt(value);
        }
        return 0;
    }

    @Override
    public JSONObject selectJSONByKey(String key) {
        String value = this.selectByKey(key);
        if (StringUtils.isNotBlank(value)){
            return JSON.parseObject(value);
        }
        return new JSONObject();
    }

    @Override
    public void insert(String key, String value, String desc) {
        Dict dict = new Dict();
        dict.setId(UUIDUtils.get());
        dict.setDictDescription(desc);
        dict.setDictKey(key);
        dict.setDictValue(value);
        dictMapper.insert(dict);
    }

    @Override
    public List<Dict> select(String key) {
        Dict dict = new Dict();
        if (StringUtils.isNotBlank(key)){
            dict.setDictKey(key);
        }
        return dictMapper.selectList(dict);
    }

    @Override
    public void update(Dict dict) {
        dictMapper.updateByPrimaryKeySelective(dict);
        if (dict.getDictKey().startsWith("SLEEP_")){
            changeRunningContanst.sleep();
        }else if (dict.getDictKey().startsWith("RETRY_")){
            changeRunningContanst.retry();
        }
    }
}
