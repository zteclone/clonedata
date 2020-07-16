package com.zte.clonedata.job.maoyan;

import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.SleepContanst;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.AbstractJob;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.HttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.douban
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 13:58 2020/6/4
 * @Description:
 */
@Slf4j
public class JobMaoyanMvDetail extends Thread {

    public static int successCount = 0;

    private List<Mv> list;
    private MvMapper mvMapper;

    public JobMaoyanMvDetail(List<Mv> list, MvMapper mvMapper) {
        this.mvMapper = mvMapper;
        this.list = list;
    }

    @SneakyThrows
    @Override
    public void run() {
        String date = DateUtils.getNowYYYYMMDDHHMMSS();
        for (Mv mv : list) {
            mv.setpDate(date);
            try {
                getMoviceSave(mv);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }


    private int c = 0;

    private void getMoviceSave(Mv maoyanMv) throws InterruptedException {
        try {
            String result = HttpUtils.getJson(maoyanMv.getUrl(), Contanst.MAOYAN_HOST1, HttpType.MAOYAN_DETAIL);
            Document doc = Jsoup.parse(result);
            Elements select = doc.select("div[class=\"celebrity-group\"]");
            for (Element element : select) {
                String t1 = element.child(0).text();
                String t2 = element.child(1).text();
                if (t1.equals("导演")){
                    maoyanMv.setDirector(t2);
                }else if (t1.equals("演员")){
                    maoyanMv.setActors(t2);
                }else if (t1.contains("编剧")){
                    maoyanMv.setScenarist(t2);
                }
            }
            Elements children = doc.select("div[class=\"celeInfo-right clearfix\"]").get(0).children().get(0).children();
            maoyanMv.setMoviename(children.get(0).text());
            maoyanMv.setAka(children.get(1).text());
            Elements c2 = children.get(2).children();
            maoyanMv.setType(c2.get(0).text());
            String[] split = c2.get(1).text().split("/");
            if (split.length == 2){
                maoyanMv.setCountry(split[0]);
                maoyanMv.setRuntime(split[1]);
            }else if (split.length == 1){
                if (split[0].endsWith("分钟")){
                    maoyanMv.setRuntime(split[1]);
                }else {
                    maoyanMv.setCountry(split[0]);
                }
            }
            maoyanMv.setReleasedata(c2.get(2).text());
            maoyanMv.setMoviedesc(doc.select("span[class=\"dra\"]").text());
            c = 0;
            synchronized (this){
                successCount ++;
            }
            mvMapper.insertSelective(maoyanMv);
            Thread.sleep(SleepContanst.SLEEP_DETAIL_SPAN_TIME);
        } catch (Exception e) {
            log.error("发生错误url >>> {}", maoyanMv.getUrl());
            if (e instanceof BusinessException){
                log.error("获取详单错误 >>> {}", ((BusinessException) e).getCommonError().getErrorMsg());
                if (((BusinessException) e).getCommonError().getErrorMsg().contains("HttpStatus: 4")) return;
            }else if (e instanceof DataIntegrityViolationException){
                log.error("存入数据库异常,请检查数据库配置及字段. >>> {}",e.getMessage());
                return;
            }else {
                log.error("获取详单错误 >>> {}", e.getMessage());
            }
            if (c++ < 10) {
                log.error("{} 后再次尝试获取详单，次数:  >>>{}<<<",SleepContanst.SLEEP_DETAIL_ERROR_SPAN_TIME, c);
                Thread.sleep(SleepContanst.SLEEP_DETAIL_ERROR_SPAN_TIME);
                getMoviceSave(maoyanMv);
            } else {
                c = 0;
                maoyanMv = null;
            }
        }
    }

    public static void main(String[] args) throws BusinessException, IOException {
        List<Mv> list = new ArrayList<>();
        //for (int i = 1301740; i < 1301930; i++) {
            String url = "https://maoyan.com/films/246071";
            Mv maoyanMv = new Mv();
            maoyanMv.setUrl(url);
            list.add(maoyanMv);
        //}
        JobMaoyanMvDetail jobmaoyanMvDetail = new JobMaoyanMvDetail(list,null);
        jobmaoyanMvDetail.start();
    }
}
