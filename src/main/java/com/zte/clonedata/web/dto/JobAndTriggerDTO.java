package com.zte.clonedata.web.dto;

import lombok.Data;

/**
 * ProjectName: clonedata-com.zte.clonedata.web.dto
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 15:33 2020/6/18
 * @Description:
 */
@Data
public class JobAndTriggerDTO {
    private String JOB_NAME;
    private String JOB_GROUP;
    private String TRIGGER_NAME;
    private String CRON_EXPRESSION;
    private String JOB_STATUS;
}
