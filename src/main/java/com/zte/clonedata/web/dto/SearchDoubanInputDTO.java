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
public class SearchDoubanInputDTO implements Serializable {
    private String cover_url;
    private String url;
    private String title;
    private Rating rating;
    private String id;

    public String getRating(){
        if (rating == null){
            return "";
        }
        return this.rating.value;
    }
    @Data
    class Rating{
        private String value;
    }
}