package com.zte.clonedata.job.maoyan;

import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.ChangeRunningContanst;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.job.utils.DetailUtils;
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

/**
 * ProjectName: clonedata-com.zte.clonedata.job.douban
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 13:58 2020/6/4
 * @Description:
 */
@Slf4j
public class JobMaoyanMvDetail extends Thread {

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
                if (e instanceof DataIntegrityViolationException) {
                    log.error("详单存入数据库异常,请检查数据库配置及字段. >>> {}", e.getMessage());
                }else{
                    e.printStackTrace();
                    log.error("详单执行错误！原因：{}",e.getMessage());
                }
            }
        }
    }

    private void getMoviceSave(Mv maoyanMv) throws InterruptedException {
        String result = DetailUtils.getHtmlData(maoyanMv.getUrl(), 0, Contanst.MAOYAN_HOST1,HttpType.MAOYAN_MV);
        if (StringUtils.isBlank(result)) return;

        Document doc = Jsoup.parse(result);
        Elements select = doc.select("div[class=\"celebrity-group\"]");
        for (Element element : select) {
            String t1 = element.child(0).text();
            String t2 = element.child(1).text();
            if (t1.equals("导演")) {
                maoyanMv.setDirector(t2);
            } else if (t1.equals("演员")) {
                maoyanMv.setActors(t2);
            } else if (t1.contains("编剧")) {
                maoyanMv.setScenarist(t2);
            }
        }
        Elements children = doc.select("div[class=\"celeInfo-right clearfix\"]").get(0).children().get(0).children();
        maoyanMv.setMoviename(children.get(0).text());
        maoyanMv.setAka(children.get(1).text());
        Elements c2 = children.get(2).children();
        maoyanMv.setType(c2.get(0).text());
        String[] split = c2.get(1).text().split("/");
        if (split.length == 2) {
            maoyanMv.setCountry(split[0]);
            maoyanMv.setRuntime(split[1]);
        } else if (split.length == 1) {
            if (split[0].endsWith("分钟")) {
                maoyanMv.setRuntime(split[1]);
            } else {
                maoyanMv.setCountry(split[0]);
            }
        }
        maoyanMv.setReleasedata(c2.get(2).text());
        maoyanMv.setMoviedesc(doc.select("span[class=\"dra\"]").text());
        mvMapper.insertSelective(maoyanMv);
        Thread.sleep(20000);
    }

    public static void main(String[] args) throws InterruptedException {
        String url = "https://maoyan.com/films/346089";
        Mv mv = new Mv();
        mv.setUrl(url);
        JobMaoyanMvDetail detail = new JobMaoyanMvDetail(null,null);
        detail.getMoviceSave(mv);
    }
}
