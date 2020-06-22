package com.zte.clonedata.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zte.clonedata.job.douban.JobDoubanMv;
import com.zte.clonedata.job.douban.JobDoubanTv;
import com.zte.clonedata.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zte.clonedata.contanst.Contanst.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 12:15 2020/6/1
 * @Description:
 */
@RestController
@RequestMapping("/douban")
@Slf4j
public class DoubanWeb {


    @Autowired
    private JobDoubanMv jobDoubanMv;
    @Autowired
    private JobDoubanTv jobDoubanTv;

    @GetMapping("/doubanMv")
    public ResponseUtils doubanMv(@RequestParam("paramType") String paramType) throws InterruptedException {
        String ps = "^(\\d{2})(\\d{2})(\\d{2})$";;
        Pattern p = Pattern.compile(ps);
        Matcher matcher = p.matcher(paramType);
        int c, y1, y2;
        if (
                !matcher.find()
                        || (c = Integer.parseInt(matcher.group(1))) > countrysLen
                        || (y1 = Integer.parseInt(matcher.group(2))) > yearsLen
                        || (y2 = Integer.parseInt(matcher.group(3))) > yearsLen) {
            return ResponseUtils.fail(
                    String.format("参数错误, 参考参数: <br /><br />参数第一位: 0 < x <= %s<br /><br />参数第二,三位: 0 < x <= %s<br /><br />例如: 010101,100101",
                            countrysLen, yearsLen));
        }
        String execute = jobDoubanMv.execute(countrys[c - 1], years[y1 - 1], years[y2 - 1]);
        if (execute.contains("失败")) {
            return ResponseUtils.fail(execute);
        }
        return ResponseUtils.success(execute);
    }
    @GetMapping("/getParam")
    public ResponseUtils getParam(){
        List<String> countryList = Lists.newLinkedList();
        List<String> yearList = Lists.newLinkedList();
        for (int i = 0; i < countrysLen; i++) {
            if (i<9){
                countryList.add("0".concat(String.valueOf(i+1)).concat("-").concat(countrys[i]));
            }else {
                countryList.add(String.valueOf(i+1).concat("-").concat(countrys[i]));
            }
        }
        for (int i = 0; i < yearsLen; i++) {
            if (i<9){
                yearList.add("0".concat(String.valueOf(i+1)).concat("-").concat(years[i]));
            }else {
                yearList.add(String.valueOf(i+1).concat("-").concat(years[i]));
            }
        }
        if (countrysLen != yearsLen){
            if (countrysLen>yearsLen){
                for (int i = 0; i < countrysLen - yearsLen; i++) {
                    yearList.add("");
                }
            }else {
                for (int i = 0; i < yearsLen - countrysLen; i++) {
                    countryList.add("");
                }
            }
        }
        Map<String, List> result = Maps.newHashMap();
        result.put("countryList",countryList);
        result.put("yearList",yearList);
        return ResponseUtils.successData(result);
    }

    @GetMapping("/doubanTv")
    public ResponseUtils doubanTv(@RequestParam("paramType") String paramType) throws InterruptedException {
        String ps = "^(\\d{2})(\\d{2})(\\d{2})$";;
        Pattern p = Pattern.compile(ps);
        Matcher matcher = p.matcher(paramType);
        int c, y1, y2;
        if (
                !matcher.find()
                        || (c = Integer.parseInt(matcher.group(1))) > countrysLen
                        || (y1 = Integer.parseInt(matcher.group(2))) > yearsLen
                        || (y2 = Integer.parseInt(matcher.group(3))) > yearsLen) {
            return ResponseUtils.fail(
                    String.format("参数错误, 参考参数: <br /><br />参数第一位: 0 < x <= %s<br /><br />参数第二,三位: 0 < x <= %s<br /><br />例如: 010101,100101",
                            countrysLen, yearsLen));
        }
        String execute = jobDoubanTv.execute(countrys[c - 1], years[y1 - 1], years[y2 - 1]);
        if (execute.contains("失败")) {
            return ResponseUtils.fail(execute);
        }
        return ResponseUtils.success(execute);
    }

}
