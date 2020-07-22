package com.zte.clonedata.web;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.clonedata.contanst.ExcelConstant;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.MvExample;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.EsayExcelExportUtils;
import com.zte.clonedata.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
@Slf4j
public class MvWeb {

    @Autowired
    private MvMapper mvMapper;
    @Autowired
    private EsayExcelExportUtils esayExcelExportUtils;

    @GetMapping("/list")
    public ResponseUtils list(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "moviename", defaultValue = "") String moviename) {
        PageHelper.startPage(page, limit);
        MvExample mvExample = new MvExample();
        mvExample.setOrderByClause("p_date desc,mv_typeid,movieid");
        MvExample.Criteria criteria = mvExample.createCriteria();
        if (StringUtils.isNotBlank(moviename)) {
            criteria.andMovienameLike("%" + moviename + "%");
        }
        List<Mv> dictList = mvMapper.selectByExample(mvExample);
        PageInfo<Mv> pages = new PageInfo<>(dictList);
        return ResponseUtils.success((int) pages.getTotal(), dictList);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<Mv> mvs = mvMapper.selectByExample(null);
        String filename = "clonedata_mv_".concat(DateUtils.getNowYYYYMMDDHHMMSS());
        try {
            esayExcelExportUtils.exportExcel_small(mvs, filename, null, response, Mv.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
