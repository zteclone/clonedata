package com.zte.clonedata.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.clonedata.model.Dict;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.service.PageNoService;
import com.zte.clonedata.util.ResponseUtils;
import com.zte.clonedata.web.dto.PageNoDTO;
import com.zte.clonedata.web.validator.ValidationResult;
import com.zte.clonedata.web.validator.ValidatorImpl;
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
@RequestMapping("/pageNo")
public class PageNoWeb {

    @Autowired
    private PageNoService pageNoService;
    @Autowired
    private ValidatorImpl validator;

    @GetMapping("/list")
    public ResponseUtils list(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "key",defaultValue = "")String key) {
        PageHelper.startPage(page,limit);
        List<PageNoDTO> pageNoList = pageNoService.select(key);
        PageInfo<PageNoDTO> pages = new PageInfo<>(pageNoList);
        return ResponseUtils.success((int) pages.getTotal(), pageNoList);
    }

    @PostMapping("/update")
    public ResponseUtils update(@RequestBody PageNo pageNo){
        ValidationResult validator = this.validator.validator(pageNo);
        if (validator.isHasErrors()){
            return ResponseUtils.fail(validator.getErrorMsg());
        }
        if (StringUtils.isNumeric(pageNo.getValue()) && Integer.parseInt(pageNo.getValue()) >= 0){
            pageNoService.updateValueByIdAndTypeid(pageNo);
        }else {
            return ResponseUtils.fail("参数错误,页数必须为数字,且大于等于0");
        }
        return ResponseUtils.success("修改成功");
    }

}
