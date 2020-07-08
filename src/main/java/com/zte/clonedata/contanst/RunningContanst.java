package com.zte.clonedata.contanst;

import com.zte.clonedata.service.DataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ProjectName: clonedata-com.zte.clonedata.contanst
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:28 2020/6/16
 * @Description:
 */
@Component
public class RunningContanst {

    /**
     * 豆瓣 id
     */
    public static String TYPE_DOUBAN_ID;
    /**
     * 豆瓣 id
     */
    public static String TYPE_MAOYAN_ID;




    @Autowired
    private DataTypeService dataTypeService;
    @PostConstruct
    public void init(){
        TYPE_DOUBAN_ID = dataTypeService.getId(Contanst.DOUBAN);
        TYPE_MAOYAN_ID = dataTypeService.getId(Contanst.MAOYAN);
    }


}
