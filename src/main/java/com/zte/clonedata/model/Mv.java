package com.zte.clonedata.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Mv implements Serializable {
    private String id;

    private String movieid;

    private Integer type;

    private static final long serialVersionUID = 1L;

}