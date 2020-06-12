package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskManagement implements Serializable {
    private String id;

    private String excuteTarget;

    private String externalCode;

    private String taskExcutePlan;

    private String taskId;

    private String taskName;

    private String taskStatus;

    private String taskType;

    private Short timeoutSecond;

    private int jobRun;

    private static final long serialVersionUID = 1L;

}