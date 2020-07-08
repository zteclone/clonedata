package com.zte.clonedata.service;

import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.web.dto.PageNoDTO;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.service
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:51 2020/7/1
 * @Description:
 */
public interface PageNoService {

    void insert(PageNo pageNo);

    void updateValueByKeyAndTypeid(PageNo pageNo);
    void updateValueByIdAndTypeid(PageNo pageNo);

    String getValueByKeyAndTypeid(PageNo pageNo);

    List<PageNoDTO> select(String key);
}
