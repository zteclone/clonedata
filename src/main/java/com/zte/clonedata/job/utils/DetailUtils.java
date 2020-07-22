package com.zte.clonedata.job.utils;

import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.utils
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:43 2020/7/21
 * @Description:
 */
@Slf4j
public class DetailUtils {

    public static String getHtmlData(String url, int c,String agentHost,HttpType httpType) throws InterruptedException {
        try {
            return HttpUtils.getJson(url, agentHost, httpType);
        } catch (Exception e) {
            log.error("发生错误url >>> {}", url);
            if (e instanceof BusinessException){
                BusinessException err = (BusinessException) e;
                log.error("获取详单错误 >>> {}", err.getCommonError().getErrorMsg());
                if (err.getCommonError().getErrorMsg().contains("HttpStatus: 4")) return null;
            }else {
                log.error("获取详单错误 >>> {}", e.getMessage());
            }
            if (c++ < ChangeRunningContanst.RETRY_DETAIL_COUNT) {
                log.error("{} 毫秒后再次尝试获取详单，次数:  >>>{}<<<", ChangeRunningContanst.SLEEP_DETAIL_ERROR_SPAN_TIME, c);
                Thread.sleep(ChangeRunningContanst.SLEEP_DETAIL_ERROR_SPAN_TIME);
                return getHtmlData(url, c,agentHost,httpType);
            }
        }
        return null;
    }

}
