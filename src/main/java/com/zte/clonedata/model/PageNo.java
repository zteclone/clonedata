package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 页数表
 */
@Data
public class PageNo implements Serializable {
    private String id;

    private String key;

    private String value;

    private String type;

    private String updateDt;

    private static final long serialVersionUID = 1L;

    public PageNo(String key,String type){
        this.key = key;
        this.type = type;
    }

    public PageNo(){

    }
}