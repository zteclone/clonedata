package com.zte.clonedata.web.jobweb;

import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.job.maoyan.JobMaoyanMv;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 12:15 2020/6/1
 * @Description:
 */
@RestController
@RequestMapping("/maoyan")
@Slf4j
public class MaoyanWeb {


    @Autowired
    private JobMaoyanMv jobMaoyanMv;


    @GetMapping("/maoyanMv")
    public ResponseUtils doubanMv(@RequestParam("paramType") String paramType) throws InterruptedException {
        String ps = "^(\\d{2})(\\d{2})(\\d{0,2})$";
        Pattern p = Pattern.compile(ps);
        Matcher matcher = p.matcher(paramType);
        if (!matcher.find()) {
            return ResponseUtils.fail("参数错误 - 位数");
        }
        String execute = "请求失败, 原因: 该功能正在开发中";//jobMaoyanMv.execute(matcher.group(1), matcher.group(2), null);
        if (execute.contains("失败")) {
            return ResponseUtils.fail(execute);
        }
        return ResponseUtils.success(execute);
    }

}
