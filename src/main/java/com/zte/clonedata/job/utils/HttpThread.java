package com.zte.clonedata.job.utils;

import com.google.common.collect.Maps;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.douban.JobDoubanMvDetail;
import com.zte.clonedata.job.douban.JobDoubanTvDetail;
import com.zte.clonedata.job.maoyan.JobMaoyanMvDetail;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.PicDownUtils;
import com.zte.clonedata.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 11:00 2020/7/21
 * @Description:
 *
 *
 *  单独一次任务执行
 */
@Slf4j
public class HttpThread extends Thread {

    private String url;
    private HttpType httpType;
    private String errMsg;
    private boolean pageLock = true;
    private int addCount = 0;

    public HttpThread(String url, HttpType httpType) throws InterruptedException {
        this.url = url;
        this.httpType = httpType;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        PicDownUtils picDownUtils = new PicDownUtils();
        Map<String, Object> map = Maps.newHashMap();
        JobConvertStringToObject bean = SpringContextUtil.getBean(JobConvertStringToObject.class);
        try {
            if (HttpType.DOUBAN_MY.equals(httpType)) {
                bean.getDoubanMv(url, picDownUtils, map);
                List<Mv> mvs = new ArrayList(map.values());
                JobDoubanMvDetail detail = new JobDoubanMvDetail(mvs, SpringContextUtil.getBean(MvMapper.class));
                detail.start();
            }else if (HttpType.DOUBAN_TY.equals(httpType)){
                bean.getDoubanTv(url, picDownUtils, map);
                List<DoubanTv> tvs = new ArrayList(map.values());
                JobDoubanTvDetail detail = new JobDoubanTvDetail(tvs, SpringContextUtil.getBean(DoubanTvMapper.class));
                detail.start();
            }else if (HttpType.MAOYAN_MV.equals(httpType)){
                bean.getmaoyanMv(url, picDownUtils, map);
                List<Mv> mvs = new ArrayList(map.values());
                JobMaoyanMvDetail detail = new JobMaoyanMvDetail(mvs, SpringContextUtil.getBean(MvMapper.class));
                detail.start();
            }
        } catch (BusinessException e) {
            if (e.getCommonError().getErrorCode() == 20002)pageLock = false;
            errMsg = e.getCommonError().getErrorMsg();
        }catch (Exception e){
            errMsg = e.getMessage();
        }finally {
            if (StringUtils.isBlank(errMsg)){
                log.info("请求成功");
            }else {
                log.error("请求失败：{}",errMsg);
            }
        }
        if (picDownUtils.files.size() != 0) {
            Thread t1 = new Thread(picDownUtils);
            t1.start();
        }
        addCount = map.size();
        Map<String,String> msgMap = Maps.newHashMap();
        msgMap.put("任务URL: ",url);
        msgMap.put("任务类型: ",httpType.name());
        msgMap.put("任务成功个数: ",String.valueOf(addCount));
        msgMap.put("任务错误信息: ",errMsg);
        log.debug("单条任务信息: {}",msgMap);
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
