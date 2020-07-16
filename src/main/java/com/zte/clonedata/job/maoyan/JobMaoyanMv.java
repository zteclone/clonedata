package com.zte.clonedata.job.maoyan;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.contanst.SleepContanst;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.PageNo;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.service.DictService;
import com.zte.clonedata.util.HttpUtils;
import com.zte.clonedata.util.PicDownUtils;
import com.zte.clonedata.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.maoyan
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 17:38 2020/6/23
 * @Description:
 */
@Component
@Slf4j
public class JobMaoyanMv extends AbstractJob {

    @Autowired
    private DictService dictService;
    private int c = 0;

    public String execute(String counrty, String year1, String year2) throws InterruptedException {
        JSONObject countrysJson = dictService.selectJSONByKey("maoyan_countrys_json");
        JSONObject yearsJson = dictService.selectJSONByKey("maoyan_years_json");
        String countryname = (String) countrysJson.get(counrty);
        String yearname = (String) yearsJson.get(year1);
        if (StringUtils.isBlank(countryname) || StringUtils.isBlank(yearname)) {
            return "失败: 参数错误 - 数字";
        }
        String key ="猫眼电影 - ".concat(countryname).concat(yearname);
        ExecutorService exe = Executors.newCachedThreadPool();
        log.info("猫眼电影开始执行任务   >>>");
        //检查主目录
        checkBasePath(Contanst.BASEURL.concat(Contanst.TYPE_MAOYAN));
        Map<String, Mv> maoyanMvMap = Maps.newHashMap();
        PicDownUtils picDownUtils = new PicDownUtils();
        boolean isLock = false;
        String executeResult = null;
        int thisc = 0;
        synchronized (this) {
            //查询该任务的起始页数
            PageNo pageNo = new PageNo(key, RunningContanst.TYPE_MAOYAN_ID);
            String value = pageNoService.getValueByKeyAndTypeid(pageNo);

            int start = Integer.parseInt(value);

            while (true) {
                String url = String.format(
                        "https://maoyan.com/films?sourceId=%s&yearId=%s&showType=3&offset=%s",
                        counrty, year1, start);
                try {
                    getListByURL(url, picDownUtils, maoyanMvMap);
                    log.info("key => {}, start => {}", key, start);
                } catch (BusinessException e) {
                    if (e.getCommonError().getErrorCode() == 20002) {
                        break;
                    }
                    isLock = true;
                    executeResult = "发生错误! 可能原因: ".concat(e.getCommonError().getErrorMsg());
                    break;
                } catch (Exception e) {
                    isLock = true;
                    executeResult = "发生错误! 可能原因: ".concat(e.getMessage());
                    break;
                }
                thisc = thisc + 30;
                start = start + 30;
                if (thisc >= 600) {
                    isLock = true;
                    log.info("此次收集电影信息已达600,暂停此次任务,以保证下时段IP安全 ... >>> country: {}, year: {}-{}", counrty, year1, year2);
                    break;
                }
                if (start >= 3000) {
                    log.info("此段收集电影信息已达3000,结束此段任务 ... >>> country: {}, year: {}-{}", counrty, year1, year2);
                    break;
                }
                Thread.sleep(SleepContanst.SLEEP_INDEX_SPAN_TIME);
            }
            //修改页数
            updatePageNo(isLock, start, pageNo);

            log.info("猫眼 {} 条数据加载完毕,即将爬取这些数据的详情页面   >>>", maoyanMvMap.size());
            if (picDownUtils.files.size() != 0) {
                Thread t1 = new Thread(picDownUtils);
                exe.execute(t1);
            }
            executeDetail(maoyanMvMap, exe);
            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    break;
                }
                Thread.sleep(SleepContanst.SLEEP_RUN_SPAN_TIME);
            }
        }
        if (executeResult == null) {
            executeResult = String.format("请求成功,新增电影: %s 条", JobMaoyanMvDetail.successCount);
        } else {
            executeResult = String.format("请求成功,新增电影: %s 条, 请求过程中%s", JobMaoyanMvDetail.successCount, executeResult);
        }
        JobMaoyanMvDetail.successCount = 0;
        log.info(executeResult);
        return executeResult;
    }

    private void updatePageNo(boolean isLock, Integer start, PageNo pageNo) {
        if (isLock) {
            pageNo.setValue(String.valueOf(start));
        } else {
            pageNo.setValue("0");
        }
        pageNoService.updateValueByKeyAndTypeid(pageNo);
    }

    protected <T> void getListByURL(String url, PicDownUtils picDownUtils, Map<String, T> maoyanMvMap) throws InterruptedException, BusinessException {
        try {
            String result = HttpUtils.getJson(url, Contanst.MAOYAN_HOST1, HttpType.MAOYAN);
            Document parse = Jsoup.parse(result);
            Elements select = parse.select("div[class=\"movies-list\"]");
            if (select.size()!= 0){
                Element divDl = select.get(0);
                if (divDl.childNodeSize() != 0){
                    Element dl = divDl.child(0);
                    if (dl.childNodeSize() == 1){
                        throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
                    }
                    for (int i = 0; i < dl.childNodeSize()-1; i++) {
                        Element dd = dl.child(i);
                        String img = dd.select("img[class=\"movie-hover-img\"]").attr("src");
                        String mvUrl = dd.select("a").get(0).attr("href");
                        String id = mvUrl.replace("/films/", "");
                        String name2 = dd.select("div[class=\"channel-detail movie-item-title\"]").text();
                        String num2 = dd.select("div[class=\"channel-detail channel-detail-orange\"]").text();
                        if (num2.equals("暂无评分")){
                            num2 = "";
                        }
                        String path = Contanst.BASEURL.concat(Contanst.TYPE_MAOYAN).concat(File.separator).concat(id).concat(".jpg");


                        Mv m = new Mv();
                        m.setMovieid(id);
                        m.setMvTypeid(RunningContanst.TYPE_MAOYAN_ID);
                        Mv mv = mvMapper.selectByPrimaryKey(m);
                        if (mv == null) {
                            //不存在
                            m.setUrl(Contanst.MAOYAN_HTTPS.concat(mvUrl));
                            m.setHttpImage(img);
                            m.setFilepath(path);
                            m.setMovieid(id);
                            m.setRatingnum(num2);
                            m.setMoviename(name2);
                            maoyanMvMap.put(m.getMovieid(), (T) m);
                        } else {
                            if (!mv.getRatingnum().equals(num2)) {
                                //分数不同
                                m.setRatingnum(num2);
                                mvMapper.updateByPrimaryKeySelective(m);
                            }
                        }

                        /**
                         * 1. 存在   不处理
                         * 2. 不存在 加入任务
                         */
                        File file = new File(path);
                        if (file.exists()) {
                            continue;
                        } else {
                            picDownUtils.urls.add(img);
                            picDownUtils.files.add(file);
                        }
                    }
                }
            }else {
                throw  new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
            }

        } catch (BusinessException e) {
            if (e.getCommonError().getErrorCode() == 20002 || e.getCommonError().getErrorCode() == 20003) {
                throw e;
            }
            if (c++ < 10) {
                log.error("发生错误url >>> {}", url);
                log.error("{} 后再次尝试连接，次数:  >>>{}<<<",SleepContanst.SLEEP_INDEX_ERROR_SPAN_TIME, c);
                Thread.sleep(SleepContanst.SLEEP_INDEX_ERROR_SPAN_TIME);
                getListByURL(url, picDownUtils, maoyanMvMap);
            } else {
                throw e;
            }
        } finally {
            c = 0;
        }
    }


    protected <T> void executeDetail(Map<String, T> movies, ExecutorService exe) {
        int size = movies.size() / spList;
        int b = movies.size() % spList;
        log.info("计划创建: {}条任务  >>>", b == 0 ? size : (size + 1));
        log.info("开始执行计划任务项  >>>");
        /**
         * 防止一次任务多个相同id查出后,查询详情插入数据库的主键错误
         */
        List<Mv> mvs = (List<Mv>) movies.values().stream().collect(Collectors.toList());
        for (int i = 0; i < size; i++) {
            List<Mv> m1 = new ArrayList<>(mvs.subList((i * spList), (i + 1) * spList));
            exe.execute(new JobMaoyanMvDetail(m1, mvMapper));
        }
        if (b != 0) {
            List<Mv> m1 = new ArrayList<>(mvs.subList(size * spList, movies.size()));
            exe.execute(new JobMaoyanMvDetail(m1, mvMapper));
        }
    }

    public static void main(String[] args) throws BusinessException, InterruptedException {
        String url = "https://maoyan.com/films?sourceId=03&yearId=15&showType=3&offset=2220";
        JobMaoyanMv jobMaoyanMv = new JobMaoyanMv();
        jobMaoyanMv.getListByURL(url,null,null);
    }
}