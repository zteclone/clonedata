package com.zte.clonedata.service.impl;

import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DataTypeMapper;
import com.zte.clonedata.model.DataType;
import com.zte.clonedata.service.DataTypeService;
import com.zte.clonedata.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProjectName: clonedata-com.zte.clonedata.service.impl
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:48 2020/7/1
 * @Description:
 */
@Service
public class DataTypeServiceImpl implements DataTypeService {

    @Autowired
    private DataTypeMapper dataTypeMapper;

    @Override
    public String getId(String typeName) {
        String id = dataTypeMapper.getIdByTypeName(typeName);
        if (StringUtils.isBlank(id)){
            id = UUIDUtils.get();
            dataTypeMapper.insert(new DataType(id, typeName));
        }
        return id;
    }
}
