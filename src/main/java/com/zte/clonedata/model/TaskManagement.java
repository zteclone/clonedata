package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskManagement implements Serializable {
    private String id;//任务id
    private String excuteTarget;//任务url
    private String externalCode;//请求头参数
    private String taskExcutePlan;//执行计划cron
    private String taskId;//任务id
    private String taskName;//任务名称
    private String taskStatus;//任务状态
    private String taskType;//任务类型
    private Short timeoutSecond;//请求类型
    private int jobRun;//是否正在运行
    private String creDt;

    private static final long serialVersionUID = 1L;
}