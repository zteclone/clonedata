package com.zte.clonedata.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.MvExample;
import com.zte.clonedata.util.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:31 2020/7/7
 * @Description:
 */
@RestController
@RequestMapping("/mv")
public class MvWeb {

    @Autowired
    private MvMapper mvMapper;

    @GetMapping("/list")
    public ResponseUtils list(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "moviename",defaultValue = "")String moviename) {
        PageHelper.startPage(page,limit);
        MvExample mvExample = new MvExample();
        mvExample.setOrderByClause("p_date desc,mv_typeid,movieid");
        MvExample.Criteria criteria = mvExample.createCriteria();
        if (StringUtils.isNotBlank(moviename)){
            criteria.andMovienameLike("%"+moviename+"%");
        }
        List<Mv> dictList = mvMapper.selectByExample(mvExample);
        PageInfo<Mv> pages = new PageInfo<>(dictList);
        return ResponseUtils.success((int) pages.getTotal(), dictList);
    }
}
