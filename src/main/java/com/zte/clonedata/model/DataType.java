package com.zte.clonedata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类别表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataType implements Serializable {
    private String id;
    private String typeName;
    private static final long serialVersionUID = 1L;
}