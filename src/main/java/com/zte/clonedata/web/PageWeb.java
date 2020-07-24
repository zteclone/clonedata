package com.zte.clonedata.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.service.TaskManagementService;
import com.zte.clonedata.util.*;
import com.zte.clonedata.web.dto.MvDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 12:22 2020/6/1
 * @Description:
 */
@RestController
@RequestMapping("/page")
@Slf4j
public class PageWeb {
    @Autowired
    private TaskManagementService taskManagementService;
    private PoolingHttpClientConnectionManager connManager;
    @Autowired
    private MvMapper mvMapper;
    @Autowired
    private DoubanTvMapper doubanTvMapper;
    @Autowired
    private RunningContanst runningContanst;


    @GetMapping("/list")
    public ResponseUtils list(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "taskname",defaultValue = "")String taskname) throws Exception {
        PageHelper.startPage(page,limit);
        List<TaskManagement> taskManagements = taskManagementService.select(taskname);
        Set<JobKey> jobKeys = ScheduleUtils.getScheduler().getJobKeys(GroupMatcher.anyGroup());
        Set<String> nameList = new HashSet<>();
        for (JobKey jobKey : jobKeys) {
            nameList.add(jobKey.getName());
        }
        taskManagements.stream().forEach(x ->{
            if (nameList.contains(x.getTaskId())){
                x.setJobRun(1);
                x.setNextExecuteDate(DateUtils.getString(CronUtils.getNextExecution(x.getTaskExcutePlan())));
            }else {
                x.setJobRun(0);
            }


        });
        PageInfo<TaskManagement> pages = new PageInfo<>(taskManagements);
        return ResponseUtils.success((int) pages.getTotal(), taskManagements);
    }


    /**
     * 定时任务池信息
     *
     * @return
     */
    @RequestMapping("/quartzPool")
    public ResponseUtils quartzPool() throws SchedulerException {
        Set<JobKey> jobKeys = ScheduleUtils.getScheduler().getJobKeys(GroupMatcher.anyGroup());
        Map<String, Object> map = Maps.newHashMap();
        map.put("正在运行任务数",jobKeys.size());
        return ResponseUtils.successData(map);
    }

    @RequestMapping("/httpclientPoolDetail")
    public ResponseUtils httpclientPoolDetail() {
        if (connManager == null) {
            connManager = SpringContextUtil.getBean("httpClientConnectionManager");
        }
        PoolStats poolStats = connManager.getTotalStats();
        Map<String, Object> map = Maps.newHashMap();
        map.put("最大线程数", String.valueOf(poolStats.getMax()));
        map.put("空闲的线程数", String.valueOf(poolStats.getAvailable()));
        map.put("租用的线程数", String.valueOf(poolStats.getLeased()));
        map.put("即将启动的线程数", String.valueOf(poolStats.getPending()));
        return ResponseUtils.successData(map);
    }

    @RequestMapping(value = "dataCount",method = RequestMethod.GET)
    public ResponseUtils dataCount(){
        Map<String, Long> map = Maps.newHashMap();
        long mvc = mvMapper.countByExample(null);
        long tvc = doubanTvMapper.countByExample(null);
        map.put("电影总数",mvc);
        map.put("电视剧总数",tvc);
        return ResponseUtils.successData(map);
    }

    @GetMapping("/images")
    public void images(@RequestParam("movieid")String movieid,
                       @RequestParam("mvTypeid")String mvTypeid,
                       HttpServletResponse response){
        MvDTO mvDTO = mvMapper.selectTypenameByMovieidAndMvtypeid(new Mv(movieid, mvTypeid));
        this.responseImages(mvDTO.getFilepath(),response);
    }

    @GetMapping("/imagesTv")
    public void imagesTv(@RequestParam("tvid")String tvid,
                       HttpServletResponse response){
        DoubanTv doubanTv = doubanTvMapper.selectByPrimaryKey(tvid);
        this.responseImages(doubanTv.getFilepath(),response);
    }
    private void responseImages(String filepath,HttpServletResponse response){
        FileInputStream fis = null;
        ServletOutputStream out = null;
        try {
            File file = new File(filepath);
            if (!file.exists()){
                file = new File(runningContanst.BASEURL.concat("images")
                        .concat(File.separator)
                        .concat("douban")
                        .concat(File.separator)
                        .concat("tv_default_large.png"));
            }
            fis = new FileInputStream(file);
            long size = file.length();
            byte[] temp = new byte[(int) size];
            fis.read(temp, 0, (int) size);
            fis.close();
            byte[] data = temp;
            out = response.getOutputStream();
            response.setContentType("image/png");
            out.write(data);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
