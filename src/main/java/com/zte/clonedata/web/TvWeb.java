package com.zte.clonedata.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.model.*;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.util.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:31 2020/7/7
 * @Description:
 */
@RestController
@RequestMapping("/tv")
public class TvWeb {

    @Autowired
    private DoubanTvMapper doubanTvMapper;

    @GetMapping("/list")
    public ResponseUtils list(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
            ,@RequestParam(value = "tvname",defaultValue = "")String tvname) {
        PageHelper.startPage(page,limit);
        DoubanTvExample doubanTvExample = new DoubanTvExample();
        doubanTvExample.setOrderByClause("p_date desc,tvid");
        DoubanTvExample.Criteria criteria = doubanTvExample.createCriteria();
        if (StringUtils.isNotBlank(tvname)){
            criteria.andTvnameLike("%"+tvname+"%");
        }
        List<DoubanTv> doubanTvs = doubanTvMapper.selectByExample(doubanTvExample);
        PageInfo<DoubanTv> pages = new PageInfo<>(doubanTvs);
        return ResponseUtils.success((int) pages.getTotal(), doubanTvs);
    }
}
