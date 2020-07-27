package com.zte.clonedata.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ProjectName: clonedata-com.zte.clonedata.web.dto
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:04 2020/7/24
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDoubanOutputDTO implements Serializable {
    private String title;
    private String ratingnum;
    private String id;
    /*private String director;
    private String scenarist;*/
    private String actors;
    private String type;

    private String programType;
}