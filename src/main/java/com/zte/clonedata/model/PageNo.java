package com.zte.clonedata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 页数表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageNo implements Serializable {
    private String id;

    private String key;

    private String value;

    private String type;

    private String updateDt;

    private static final long serialVersionUID = 1L;
}