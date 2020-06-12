package com.zte.clonedata.web;

import com.zte.clonedata.job.douban.JobDoubanMv;
import com.zte.clonedata.job.douban.JobDoubanTv;
import com.zte.clonedata.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseUtils doubanMv() throws InterruptedException {
        String execute = jobDoubanMv.execute();
        if (execute.contains("失败")){
            return ResponseUtils.fail(execute);
        }
        return ResponseUtils.success(execute);
    }
    @GetMapping("/doubanTv")
    public ResponseUtils doubanTv() throws InterruptedException {
        return ResponseUtils.success("执行成功");
    }

}
