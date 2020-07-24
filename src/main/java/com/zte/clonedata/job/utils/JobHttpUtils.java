package com.zte.clonedata.job.utils;

import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
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
public class JobHttpUtils {

    public static String getHtmlData(String url, int c,String agentHost,HttpType httpType,boolean isMain) throws InterruptedException, BusinessException {
        try {
            return HttpUtils.getJson(url, agentHost, httpType);
        } catch (Exception e) {
            log.error("发生错误url >>> {}", url);
            if (e instanceof BusinessException){
                BusinessException err = (BusinessException) e;
                log.error("获取错误 >>> {}", err.getCommonError().getErrorMsg());
                if (err.getCommonError().getErrorMsg().contains("HttpStatus: 4")) throw new BusinessException(EmBusinessError.HTTP_400);
            }else {
                log.error("获取错误 >>> {}", e.getMessage());
            }
            if (c++ < ChangeRunningContanst.RETRY_COUNT) {
                int i;
                if (isMain){
                    i = ChangeRunningContanst.SLEEP_INDEX_ERROR_SPAN_TIME;
                }else {
                    i = ChangeRunningContanst.SLEEP_DETAIL_ERROR_SPAN_TIME;
                }
                log.error("{} 毫秒后再次尝试获取，次数:  >>>{}<<<", i, c);
                Thread.sleep(i);
                return getHtmlData(url, c,agentHost,httpType,isMain);
            }else {
                throw new BusinessException(EmBusinessError.HTTP_COUNT_MORETHAN);
            }
        }
    }

}
