package com.zte.clonedata.job;

import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.PicDownUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * ProjectName: clonedata-com.zte.clonedata.job
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 14:55 2020/6/23
 * @Description:
 */
public abstract class AbstractJob {

    /**
     * 切割集合并发访问因子
     */
    protected static final int spList = 20;

    /**
     * 任务线程主类
     * @param counrty 国家
     * @param year1 起始年份
     * @param year2 终止年份
     */
    protected abstract String execute(String counrty,String year1,String year2) throws InterruptedException;

    /**
     *  获取指定URL的集合
     * @param url   访问url
     * @param picDownUtils  图片下载工具类
     * @param dataList  url爬取数据保存集合
     * @param <T>   根据网站类型的实体类
     */
    protected abstract <T> void getListByURL(String url, PicDownUtils picDownUtils, List<T> dataList) throws InterruptedException, BusinessException;

    /**
     *  执行详单访问保存操作
     * @param dataList    需要访问详单的数据集合
     * @param exe   多线程执行类
     * @param <T>   根据网站类型的实体类
     */
    protected abstract <T> void executeDetail(List<T> dataList, ExecutorService exe);
    /**
     * 校验主目录
     */
    protected void checkBasePath() {
        //豆瓣
        File baseFile = new File(Contanst.BASEURL.concat(Contanst.TYPE_DOUBAN));
        if (!baseFile.exists()) baseFile.mkdirs();

    }
}