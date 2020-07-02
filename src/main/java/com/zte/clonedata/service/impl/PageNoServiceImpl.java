package com.zte.clonedata.service.impl;

import com.zte.clonedata.dao.PageNoMapper;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.service.PageNoService;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void insert(PageNo pageNo) {
        pageNo.setId(UUIDUtils.get());
        pageNo.setUpdateDt(DateUtils.getNowYYYYMMDDHHMMSS());
        pageNo.setValue("0");
        pageNoMapper.insert(pageNo);
    }

    @Override
    public void update(PageNo pageNo) {
        pageNo.setUpdateDt(DateUtils.getNowYYYYMMDDHHMMSS());
        pageNoMapper.updateByPrimaryKey(pageNo);
    }

    @Override
    public String getValueByKeyAndTypeid(PageNo pageNo) {
        return pageNoMapper.getValueByKeyAndTypeid(pageNo);
    }
}
