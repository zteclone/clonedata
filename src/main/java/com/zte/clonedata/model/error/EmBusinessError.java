package com.zte.clonedata.model.error;

/**
 * ProjectName: dianping-com.zte.dianping.common
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 11:29 2020/2/20 0020
 * @Description:
 */
public enum EmBusinessError {
    //1 通用错误
    NUKNOW_ERROR(10000,"未知错误"),
    IO_ERROR(10005,"流IO错误"),
    //2 网络连接
    HTTP_ERROR(20000,"网络连接异常"),
    HTTP_POOL_ERROR(20001,"初始化http连接池异常"),
    HTTP_RESULT_NULL(20002,"http返回为空"),
    HTTP_RESULT_IPLOCK(20003,"http返回IP限制"),
    //3 JOB
    JOB_ID_IS_NULL(30001,"任务id不能为空"),
    JOB_NAME_IS_NULL(30002,"任务名称不能为空"),
    JOB_CRON_IS_NULL(30003,"任务表达式不能为空"),
    ;

    private Integer errCode;
    private String errMsg;

    EmBusinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
