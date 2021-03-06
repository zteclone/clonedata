package com.zte.clonedata;

import com.zte.clonedata.config.quartz.IpPortCheckJob;
import com.zte.clonedata.config.quartz.IpPortJob;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.dao.TaskLogMapper;
import com.zte.clonedata.job.douban.HotDataJob;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.TaskLog;
import com.zte.clonedata.model.TaskLogExample;
import com.zte.clonedata.util.*;
import com.zte.clonedata.web.dto.MvDTO;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication(scanBasePackages = {"com.zte.clonedata"})
@MapperScan("com.zte.clonedata.dao")
@ServletComponentScan
@Controller
public class Application {

    /**
     * druid监控页面 >>>        http://localhost:8090/clonedata/druid/
     * maven配置 >>>           mybatis-generator:generate
     * 任务Web页面  >>>         http://localhost:8090/clonedata
     */


    public static void main(String[] args) throws SchedulerException {
        SpringApplication.run(Application.class, args);
        if (RunningContanst.IS_OPEN_IP_PROXY) {
            ScheduleUtils.addScheduleJobAndRunOne(IpPortJob.class, "ipPort", "0 0 0,5,12,18 * * ?");
        }
        FTPUtils ftpUtils = new FTPUtils();
        ftpUtils.connect();
        ftpUtils.disconnect();
    }

    @Autowired
    private TaskLogMapper taskLogMapper;
    @Autowired
    private MvMapper mvMapper;
    @Autowired
    private DoubanTvMapper doubanTvMapper;

    @PostConstruct
    public void init() {
        updateTaskLogStatusIs0();//修改任务表状态为0的为失败
    }

    private void updateTaskLogStatusIs0() {
        TaskLogExample example = new TaskLogExample();
        TaskLogExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);
        TaskLog taskLog = new TaskLog();
        taskLog.setExecuteResult("失败,可能原因: 系统关闭时关闭了正在进行的任务");
        taskLog.setStatus(2);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        taskLog.setEndtime(sdf2.format(new Date()));
        taskLogMapper.updateByExampleSelective(taskLog, example);
    }

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index.html");
        return modelAndView;
    }

    @RequestMapping("/admin/taskList")
    public ModelAndView taskList() {
        ModelAndView modelAndView = new ModelAndView("taskList.html");
        return modelAndView;
    }

    @RequestMapping("/admin/detail")
    public ModelAndView detail(@RequestParam("taskId") String taskId) {
        ModelAndView modelAndView = new ModelAndView("detail.html");
        modelAndView.addObject("taskId", taskId);
        return modelAndView;
    }

    @RequestMapping("/admin/pool")
    public ModelAndView pool() {
        ModelAndView modelAndView = new ModelAndView("pool.html");
        return modelAndView;
    }

    @RequestMapping("/admin/addJob")
    public ModelAndView addJob() {
        ModelAndView modelAndView = new ModelAndView("addJob.html");
        return modelAndView;
    }

    @RequestMapping("/admin/mv")
    public ModelAndView mv() {
        ModelAndView modelAndView = new ModelAndView("mv.html");
        return modelAndView;
    }

    @RequestMapping("/admin/tv")
    public ModelAndView tv() {
        ModelAndView modelAndView = new ModelAndView("tv.html");
        return modelAndView;
    }

    @RequestMapping("/admin/dict")
    public ModelAndView dict() {
        ModelAndView modelAndView = new ModelAndView("dict.html");
        return modelAndView;
    }

    @RequestMapping("/admin/httpSearch")
    public ModelAndView httpSearch() {
        ModelAndView modelAndView = new ModelAndView("httpSearch.html");
        return modelAndView;
    }

    @RequestMapping("/admin/pageNo")
    public ModelAndView pageNo() {
        ModelAndView modelAndView = new ModelAndView("pageNo.html");
        return modelAndView;
    }

    @RequestMapping("/admin/inithtml")
    public ModelAndView inithtml() {
        ModelAndView modelAndView = new ModelAndView("inithtml.html");
        return modelAndView;
    }

    @RequestMapping("/admin/mvDetail")
    public ModelAndView mvDetail(@RequestParam(value = "movieid", defaultValue = "") String movieid
            , @RequestParam(value = "mvTypeid", defaultValue = "") String mvTypeid) {
        ModelAndView modelAndView = new ModelAndView("mvDetail.html");
        MvDTO mvDTO = mvMapper.selectTypenameByMovieidAndMvtypeid(new Mv(movieid, mvTypeid));
        modelAndView.addObject("mv", mvDTO);
        return modelAndView;
    }

    @RequestMapping("/admin/tvDetail")
    public ModelAndView tvDetail(@RequestParam(value = "tvid", defaultValue = "") String tvid) {
        ModelAndView modelAndView = new ModelAndView("tvDetail.html");
        DoubanTv doubanTv = doubanTvMapper.selectByPrimaryKey(tvid);
        modelAndView.addObject("tv", doubanTv);
        return modelAndView;
    }
}
