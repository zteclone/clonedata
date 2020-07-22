package com.zte.clonedata.job.utils;

import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.douban.JobDoubanMvDetail;
import com.zte.clonedata.job.douban.JobDoubanTvDetail;
import com.zte.clonedata.job.maoyan.JobMaoyanMvDetail;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.CommonError;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.util.HttpUtils;
import com.zte.clonedata.util.PicDownUtils;
import com.zte.clonedata.util.SpringContextUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 11:00 2020/7/21
 * @Description:
 */
@Slf4j
public class HttpThread extends Thread {

    private String url;
    private String host;
    private HttpType httpType;
    private String errMsg;
    private boolean pageLock = true;
    private int addCount = 0;

    public HttpThread(String url, String host, HttpType httpType) throws InterruptedException {
        this.url = url;
        this.host = host;
        this.httpType = httpType;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        String data = "";
        try {
            data = getData(0);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } catch (BusinessException e) {
            errMsg = e.getCommonError().getErrorMsg();
            return;
        }
        PicDownUtils picDownUtils = new PicDownUtils();
        Map<String, Object> map = Maps.newHashMap();
        ConvertStringToObject bean = SpringContextUtil.getBean(ConvertStringToObject.class);
        try {
            if (HttpType.DOUBAN_MY.equals(httpType)) {
                bean.getDoubanMv(data, picDownUtils, map);
                List<Mv> mvs = new ArrayList(map.values());
                JobDoubanMvDetail detail = new JobDoubanMvDetail(mvs, SpringContextUtil.getBean(MvMapper.class));
                detail.start();
            }else if (HttpType.DOUBAN_TY.equals(httpType)){
                bean.getDoubanTv(data, picDownUtils, map);
                List<DoubanTv> tvs = new ArrayList(map.values());
                JobDoubanTvDetail detail = new JobDoubanTvDetail(tvs, SpringContextUtil.getBean(DoubanTvMapper.class));
                detail.start();
            }else if (HttpType.MAOYAN_MV.equals(httpType)){
                bean.getmaoyanMv(data, picDownUtils, map);
                List<Mv> mvs = new ArrayList(map.values());
                JobMaoyanMvDetail detail = new JobMaoyanMvDetail(mvs, SpringContextUtil.getBean(MvMapper.class));
                detail.start();
            }
        } catch (BusinessException e) {
            if (e.getCommonError().getErrorCode() == 20002)pageLock = false;
            errMsg = e.getCommonError().getErrorMsg();
            return;
        }
        if (picDownUtils.files.size() != 0) {
            Thread t1 = new Thread(picDownUtils);
            t1.start();
        }
        addCount = map.size();
    }

    private String getData(int c) throws InterruptedException, BusinessException {
        try {
            return HttpUtils.getJson(url, host, httpType);
        } catch (Exception e) {
            if (e instanceof BusinessException){
                BusinessException err = (BusinessException) e;
                log.error("获取错误 >>> {}", err.getCommonError().getErrorMsg());
            }else {
                log.error("获取错误 >>> {}", e.getMessage());
            }
            if (c++ < ChangeRunningContanst.RETRY_MAIN_COUNT) {
                log.error("发生错误url >>> {}", url);
                log.error("{} 毫秒后再次尝试连接，次数:  >>>{}<<<", ChangeRunningContanst.SLEEP_INDEX_ERROR_SPAN_TIME, c);
                Thread.sleep(ChangeRunningContanst.SLEEP_INDEX_ERROR_SPAN_TIME);
                return getData(c);
            } else {
                throw new BusinessException(EmBusinessError.HTTP_COUNT_MORETHAN);
            }
        }
    }

    public String getErrMsg() {
        return errMsg;
    }
    public boolean isPageLock() {
        return pageLock;
    }
    public int getAddCount() {
        return addCount;
    }
}
