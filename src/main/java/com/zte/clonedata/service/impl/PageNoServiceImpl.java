package com.zte.clonedata.service.impl;

import com.zte.clonedata.dao.PageNoMapper;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.service.PageNoService;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.UUIDUtils;
import com.zte.clonedata.web.dto.PageNoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.service.impl
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:52 2020/7/1
 * @Description:
 */
@Service
public class PageNoServiceImpl implements PageNoService {

    @Autowired
    private PageNoMapper pageNoMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void insert(PageNo pageNo) {
        pageNo.setId(UUIDUtils.get());
        pageNo.setUpdateDt(DateUtils.getNowYYYYMMDDHHMMSS());
        pageNo.setValue("0");
        pageNoMapper.insert(pageNo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateValueByKeyAndTypeid(PageNo pageNo) {
        pageNo.setUpdateDt(DateUtils.getNowYYYYMMDDHHMMSS());
        pageNoMapper.updateValueByKeyAndTypeid(pageNo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateValueByIdAndTypeid(PageNo pageNo) {
        pageNo.setUpdateDt(DateUtils.getNowYYYYMMDDHHMMSS());
        pageNoMapper.updateValueByIdAndTypeid(pageNo);
    }

    @Override
    public String getValueByKeyAndTypeid(PageNo pageNo) {
        String value = pageNoMapper.getValueByKeyAndTypeid(pageNo);
        if (StringUtils.isBlank(value)){
            this.insert(pageNo);
            value = "0";
        }
        return value;
    }

    @Override
    public List<PageNoDTO> select(String key) {
        PageNoDTO pageNoDTO = new PageNoDTO();
        pageNoDTO.setKey(key);
        return pageNoMapper.selectList(pageNoDTO);
    }
}
