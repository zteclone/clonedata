package com.zte.clonedata.util;

import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.model.error.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.util
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:17 2020/5/28
 * @Description:
 */
@Slf4j
public class PicDownUtils implements Runnable {

    public List<String> urls = new LinkedList<>();
    public List<File> files = new LinkedList<>();

    private int err = 0;
    private int c = 0;

    @Override
    public void run() {
        String nowYYYYMMDD = DateUtils.getNowYYYYMMDDHHMMSS();
        long start = System.currentTimeMillis();
        int size = urls.size();
        log.debug("{} 开始执行图片下载,保存 任务   >>>", nowYYYYMMDD);
        log.debug("图片下载个数为: {}", size);
        List<File> fileList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            String url = urls.get(i);
            File file = files.get(i);
            try {
                file = downSaveReturnFile(url, file);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } finally {
                c = 0;
            }
            if (file != null) fileList.add(file);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        log.info("成功: {}, 失败: {}, 成功率: {}", size, err, err == 0 ? "100%" : String.valueOf(df.format((double) (size - err) / (double) size * 100)).concat("%"));
        log.debug("执行图片下载,保存任务结束,用时: {}   >>>", System.currentTimeMillis() - start);
        //FTP TODO 改前缀或后缀  修改JobDoubanMv的查询是否有文件的文件名前缀后缀
            /*FTPUtils ftpUtils = SpringContextUtil.getBean(FTPUtils.class);
            ftpUtils.uploadFile(fileList);*/
    }

    private File downSaveReturnFile(String url, File file) throws InterruptedException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            HttpUtils.picGetFileSave(url, outputStream);
            return file;
        } catch (BusinessException e) {
            log.error(e.getCommonError().getErrorMsg());
            if (c++ < ChangeRunningContanst.RETRY_DETAIL_COUNT) {
                log.error("{} 毫秒后再次尝试获取图片，次数:  >>>{}<<<", ChangeRunningContanst.SLEEP_DETAIL_ERROR_SPAN_TIME, c);
                Thread.sleep(ChangeRunningContanst.SLEEP_DETAIL_ERROR_SPAN_TIME);
                return downSaveReturnFile(url, file);
            } else {
                err++;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
