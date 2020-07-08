package com.zte.clonedata.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 页数表
 */
@Data
public class PageNoDTO implements Serializable {
    @NotBlank(message = "id不能为空")
    private String id;
    private String key;
    @NotBlank(message = "页数值不能为空")
    private String value;
    @NotBlank(message = "类型id不能为空")
    private String type;
    private String typeName;
    private String updateDt;

    private static final long serialVersionUID = 1L;

    public PageNoDTO(String key, String type){
        this.key = key;
        this.type = type;
    }

    public PageNoDTO(){

    }
}