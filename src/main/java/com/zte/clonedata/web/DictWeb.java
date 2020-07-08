package com.zte.clonedata.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zte.clonedata.model.Dict;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.util.ResponseUtils;
import com.zte.clonedata.util.ScheduleUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 10:31 2020/7/7
 * @Description:
 */
@RestController
@RequestMapping("/dict")
public class DictWeb {

    @Autowired
    private DictService dictService;

    @GetMapping("/list")
    public ResponseUtils list(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "dictKey",defaultValue = "")String dictKey) {
        PageHelper.startPage(page,limit);
        List<Dict> dictList = dictService.select(dictKey);
        PageInfo<Dict> pages = new PageInfo<>(dictList);
        return ResponseUtils.success((int) pages.getTotal(), dictList);
    }

    @PostMapping("/update")
    public ResponseUtils update(@RequestBody Dict dict){
        if (StringUtils.isBlank(dict.getId())){
            return ResponseUtils.fail("参数错误,id不允许为空");
        }
        if (dict.getDictValue().contains("\"") && dict.getDictValue().contains("{") && dict.getDictValue().contains("}")){
            try {
                JSON.parseObject(dict.getDictValue());
            } catch (Exception e) {
                return ResponseUtils.fail("JSON格式校验失败");
            }
        }
        dictService.update(dict);
        return ResponseUtils.success("修改成功");
    }

}
