package com.zte.clonedata.job.douban;

import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.ChangeRunningContanst;
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
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.douban
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 13:58 2020/6/4
 * @Description:
 */
@Slf4j
public class JobDoubanMvDetail extends Thread {

    private List<Mv> list;
    private MvMapper mvMapper;
    public static int successCount = 0;
    private static Object OBJ = new Object();

    public JobDoubanMvDetail(List<Mv> list, MvMapper mvMapper) {
        this.mvMapper = mvMapper;
        this.list = list;
    }

    public JobDoubanMvDetail() {

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
                    log.error("详单执行错误！原因：{}",e.getMessage());
                }
            }
        }
    }


    public void getMoviceSave(Mv doubanMv) throws InterruptedException, BusinessException {
        log.debug("详单url：{}",doubanMv.getUrl());
        String result = JobHttpUtils.getHtmlData(doubanMv.getUrl(), 0, JobContanst.DOUBAN_HOST1,HttpType.DETAIL,false);
        if (StringUtils.isBlank(result)) return;
        Document doc = Jsoup.parse(result);
        getDoubanmvData(doubanMv,doc);
        mvMapper.insertSelective(doubanMv);
        synchronized (OBJ){
            successCount++;
        }
        TimeUnit.SECONDS.sleep(ChangeRunningContanst.SLEEP_DETAIL_SPAN_TIME);
    }

    public void getDoubanmvData(Mv doubanMv, Document doc) {
        Elements subject = doc.select("div#info");
        //导演
        Elements directs = subject.select("a[rel=\"v:directedBy\"]");
        String directName = directs.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
        if (StringUtils.isNotEmpty(directName)) {
            doubanMv.setDirector(String.format("|%s|", directName.trim()));
        }
        Elements plElement = subject.select("span[class=\"pl\"]");
        //编剧
        Optional<Element> adaptorsOpt = plElement.stream().filter(o -> o.html().equals("编剧")).findFirst();
        if (adaptorsOpt.isPresent()) {
            Element adaptorEl = adaptorsOpt.get();
            Elements adaptors = adaptorEl.nextElementSibling().select("a");
            String adaptorName = adaptors.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
            if (StringUtils.isNotEmpty(adaptorName)) {
                doubanMv.setScenarist(String.format("|%s|", adaptorName.trim()));
            }
        }
        //主演
        Optional<Element> leadOpt = plElement.stream().filter(o -> o.html().equals("主演")).findFirst();
        if (leadOpt.isPresent()) {
            Element leaderEl = leadOpt.get();
            Elements leaders = leaderEl.nextElementSibling().select("a[rel=\"v:starring\"]");
            String leaderName = leaders.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
            if (StringUtils.isNotEmpty(leaderName)) {
                doubanMv.setActors(String.format("|%s|", leaderName.trim()));
            }
        }
        //类型
        Elements kinds = subject.select("span[property=\"v:genre\"]");
        String kindName = kinds.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
        if (StringUtils.isNotEmpty(kindName)) doubanMv.setType(String.format("|%s|", kindName.trim()));
        //制片国家/地区
        Optional<Element> areaOpt = plElement.stream().filter(o -> o.html().equals("制片国家/地区:")).findFirst();
        if (areaOpt.isPresent()) {
            Element areaEl = areaOpt.get();
            String areaName = areaEl.nextSibling().outerHtml();
            if (StringUtils.isNotEmpty(areaName)) doubanMv.setCountry(String.format("|%s|", areaName.trim()));
        }
        //语言
        Optional<Element> languageOpt = plElement.stream().filter(o -> o.html().equals("语言:")).findFirst();
        if (languageOpt.isPresent()) {
            Element languageEl = languageOpt.get();
            String languageName = languageEl.nextSibling().outerHtml();
            if (StringUtils.isNotEmpty(languageName)) doubanMv.setLanguage(String.format("|%s|", languageName.trim()));
        }
        //上映日期
        Elements releaseTimeEl = subject.select("span[property=\"v:initialReleaseDate\"]");
        String releaseStr = releaseTimeEl.html();
        if (StringUtils.isNotEmpty(releaseStr))
            doubanMv.setReleasedata("|".concat(releaseStr.replaceAll("\\n", "|")).concat("|"));
        //片长
        Elements runtime = subject.select("span[property=\"v:runtime\"]");
        String runtimeStr = runtime.html();
        if (StringUtils.isNotEmpty(runtimeStr)) doubanMv.setRuntime(String.format("|%s|", runtimeStr));
        //别名
        Optional<Element> otherNameOpt = plElement.stream().filter(o -> o.html().equals("又名:")).findFirst();
        if (otherNameOpt.isPresent()) {
            Element otherNameEl = otherNameOpt.get();
            String otherName = otherNameEl.nextSibling().outerHtml();
            if (StringUtils.isNotEmpty(otherName)) doubanMv.setAka(otherName.trim());
        }
        //豆瓣评分
        Elements scores = doc.select("div#interest_sectl").select("strong[property=\"v:average\"]");
        String scoreStr = scores.html();
        if (StringUtils.isNotEmpty(scoreStr)) doubanMv.setRatingnum(scoreStr);
        //简介
        Elements storyEl = doc.select("span[property=\"v:summary\"]");
        String story = storyEl.html();
        if (StringUtils.isNotEmpty(story)) doubanMv.setMoviedesc(story.trim());
        String tag = doc.select("div.tags-body").text();
        if (StringUtils.isNotEmpty(tag)) {
            String tags = tag.replaceAll(" ", "|");
            doubanMv.setTags(tags);
        }
    }

}
