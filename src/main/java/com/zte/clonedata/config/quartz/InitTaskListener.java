package com.zte.clonedata.config.quartz;

import com.zte.clonedata.model.TaskManagement;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.service.TaskLogService;
import com.zte.clonedata.service.TaskManagementService;
import com.zte.clonedata.util.CronUtils;
import com.zte.clonedata.util.ScheduleUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.text.ParseException;
import java.util.List;

@Component
public class InitTaskListener implements InitializingBean, ServletContextAware {

    private static Logger logger = LoggerFactory.getLogger(InitTaskListener.class);

    //注入bean
    @Autowired
    private TaskLogService taskLogService;

    @Autowired
    private TaskManagementService taskManagementService;

    /**
     * 编写初始化代码
     *
     * @param servletContext
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        logger.info(">>> 系统启动完成");
        logger.info(">>> 开始装载定时任务");
        this.loadJob();
        logger.info(">>> 定时任务装载完成");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void loadJob() {
        List<TaskManagement> taskManagementList = this.taskManagementService.selectTaskManagementList();
        taskManagementList.forEach((x) -> {
            try {
                if (!StringUtils.isEmpty(x.getTaskExcutePlan()) && CronUtils.isValidExpression(x.getTaskExcutePlan())) {
                    logger.info("> 定时任务:" + x.getTaskName() + "==== 正在装载...........");
                    ScheduleUtils.addScheduleJob(x.getTaskId(), x.getTaskExcutePlan(), x.getExternalCode());
                } else {
                    logger.error("任务id：" + x.getTaskId() + " cron表达式：" + x.getTaskExcutePlan() + " 校验：" + CronUtils.isValidExpression(x.getTaskExcutePlan()));
                    x.setTaskStatus("2");
                    this.taskManagementService.updateByPrimaryKeySelective(x);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("> 装载定时任务失败", e);
            } catch (BusinessException e) {
                e.printStackTrace();
                logger.error("> 装载定时任务失败", e.getCommonError().getErrorMsg());
            } catch (SchedulerException e) {
                e.printStackTrace();
                logger.error("> 装载定时任务失败", e);
            }

        });
    }
}
