package com.zte.clonedata.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.service.TaskManagementService;
import com.zte.clonedata.util.ResponseUtils;
import com.zte.clonedata.util.ScheduleUtils;
import com.zte.clonedata.util.SpringContextUtil;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.quartz.JobKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ProjectName: clonedata-com.zte.clonedata.web
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 12:22 2020/6/1
 * @Description:
 */
@RestController
@RequestMapping("/page")
public class PageWeb {
    @Autowired
    private TaskManagementService taskManagementService;
    private ThreadPoolTaskExecutor taskExecutor;
    private PoolingHttpClientConnectionManager connManager;


    @GetMapping("/list")
    public ResponseUtils list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) throws Exception {
        PageHelper.startPage(page,limit);
        List<TaskManagement> taskManagements = taskManagementService.selectAll();
        Set<JobKey> jobKeys = ScheduleUtils.getScheduler().getJobKeys(GroupMatcher.anyGroup());
        Set<String> nameList = new HashSet<>();
        for (JobKey jobKey : jobKeys) {
            nameList.add(jobKey.getName());
        }
        taskManagements.stream().forEach(x ->{
            if (nameList.contains(x.getTaskId())){
                x.setJobRun(1);
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
    @RequestMapping("/TaskPoolDetail")
    public ResponseUtils TaskPoolDetail() {
        if (taskExecutor == null) {
            taskExecutor = SpringContextUtil.getBean("taskExecutor");
        }
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        Map<String, Object> result = Maps.newLinkedHashMap();
        result.put("正在运行", threadPoolExecutor.getActiveCount());
        result.put("已完成", threadPoolExecutor.getCompletedTaskCount());
        //result.put("线程池曾经创建过的最大线程数量", threadPoolExecutor.getLargestPoolSize());
        //result.put("最大同时运行", threadPoolExecutor.getMaximumPoolSize());
        result.put("当前线程池的线程数量", threadPoolExecutor.getPoolSize());
        result.put("任务总个数", threadPoolExecutor.getTaskCount());
        //result.put("空闲时间(秒)", threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
        return ResponseUtils.successData(result);
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

}
