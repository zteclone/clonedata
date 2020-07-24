package com.zte.clonedata.job.maoyan;

import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.job.utils.JobHttpUtils;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.DateUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.DataIntegrityViolationException;

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
    public static int successCount = 0;
    private static Object OBJ = new Object();
    /**
     * 10s * 个数 /60 = 用时（分钟）
     */
    private static final int SLEEP = 10000;

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
                }else if (e instanceof BusinessException){
                    log.error("详单执行错误！原因：{}",((BusinessException) e).getCommonError().getErrorMsg());
                }else{
                    log.error("详单执行错误！原因：美团验证未通过");
                    Thread.sleep(SLEEP);
                }
            }
        }
    }

    private void getMoviceSave(Mv maoyanMv) throws InterruptedException, BusinessException {
        log.debug("详单url：{}",maoyanMv.getUrl());
        String result = JobHttpUtils.getHtmlData(maoyanMv.getUrl(), 0, JobContanst.MAOYAN_HOST1,HttpType.MAOYAN_MV,false);
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
        synchronized (OBJ){
            successCount++;
        }
        Thread.sleep(SLEEP);
    }
}
