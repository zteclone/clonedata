package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskParam implements Serializable {
    private String id;

    private String paramName;

    private String paramValue;

    private String taskId;

    private static final long serialVersionUID = 1L;

}