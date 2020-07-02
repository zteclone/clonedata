package com.zte.clonedata.service;

import com.zte.clonedata.model.PageNo;

/**
 * ProjectName: clonedata-com.zte.clonedata.service
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:51 2020/7/1
 * @Description:
 */
public interface PageNoService {

    void insert(PageNo pageNo);

    void update(PageNo pageNo);

    String getValueByKeyAndTypeid(PageNo pageNo);

}
