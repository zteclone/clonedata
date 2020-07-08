package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Dict implements Serializable {
    private String id;
    private String dictKey;
    private String dictDescription;
    private String dictValue;
    private static final long serialVersionUID = 1L;
}