package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class IpProxy implements Serializable {
    private String id;
    private String ip;
    private Integer port;
    private Integer executeCount;
    private Integer errorCount;
    private String updDt;
    private String httpType;
    private int isOld;
    private static final long serialVersionUID = 1L;
}