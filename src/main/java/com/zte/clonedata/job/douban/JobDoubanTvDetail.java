package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanMvMapper;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.model.DoubanMv;
import com.zte.clonedata.model.DoubanTv;
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
public class JobDoubanTvDetail extends Thread {

    //TODO 豆瓣电视剧执行类


    public static int successCount = 0;

    private List<DoubanTv> list;
    private DoubanTvMapper doubanTvMapper;

    public JobDoubanTvDetail(List<DoubanTv> list, DoubanTvMapper doubanTvMapper) {
        this.doubanTvMapper = doubanTvMapper;
        this.list = list;
    }

    public JobDoubanTvDetail() {
    }
    @SneakyThrows
    @Override
    public void run() {
        String date = DateUtils.getNowYYYYMMDDHHMMSS();
        for (DoubanTv tv : list) {
            tv.setpDate(date);
            getTvSave(tv);
        }
    }


    private int c = 0;
    private int err = 0;


    private void getTvSave(DoubanTv doubanTv) throws InterruptedException {
        try {
            String result = HttpUtils.getJson(doubanTv.getUrl(), Contanst.DOUBAN_HOST1);
            Document doc = Jsoup.parse(result);
            Elements subject = doc.select("div#info");
            //导演
            Elements directs = subject.select("a[rel=\"v:directedBy\"]");
            String directName = directs.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
            if (StringUtils.isNotEmpty(directName)) {
                doubanTv.setDirector(String.format("|%s|", directName.trim()));
            }

            Elements plElement = subject.select("span[class=\"pl\"]");
            //编剧
            Optional<Element> adaptorsOpt = plElement.stream().filter(o -> o.html().equals("编剧")).findFirst();
            if (adaptorsOpt.isPresent()) {
                Element adaptorEl = adaptorsOpt.get();
                Elements adaptors = adaptorEl.nextElementSibling().select("a");
                String adaptorName = adaptors.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
                if (StringUtils.isNotEmpty(adaptorName)) {
                    doubanTv.setScenarist(String.format("|%s|", adaptorName.trim()));
                }
            }

            //主演
            Optional<Element> leadOpt = plElement.stream().filter(o -> o.html().equals("主演")).findFirst();
            if (leadOpt.isPresent()) {
                Element leaderEl = leadOpt.get();
                Elements leaders = leaderEl.nextElementSibling().select("a[rel=\"v:starring\"]");
                String leaderName = leaders.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
                if (StringUtils.isNotEmpty(leaderName)) {
                    doubanTv.setActors(String.format("|%s|", leaderName.trim()));
                }
            }

            //类型
            Elements kinds = subject.select("span[property=\"v:genre\"]");
            String kindName = kinds.stream().map(o -> o.html().trim()).collect(Collectors.joining("|"));
            if (StringUtils.isNotEmpty(kindName)) {
                doubanTv.setType(String.format("|%s|", kindName.trim()));
            }

            //制片国家/地区
            Optional<Element> areaOpt = plElement.stream().filter(o -> o.html().equals("制片国家/地区:")).findFirst();
            if (areaOpt.isPresent()) {
                Element areaEl = areaOpt.get();
                String areaName = areaEl.nextSibling().outerHtml();
                if (StringUtils.isNotEmpty(areaName)) {
                    doubanTv.setCountry(String.format("|%s|", areaName.trim()));
                }
            }

            //语言
            Optional<Element> languageOpt = plElement.stream().filter(o -> o.html().equals("语言:")).findFirst();
            if (languageOpt.isPresent()) {
                Element languageEl = languageOpt.get();
                String languageName = languageEl.nextSibling().outerHtml();
                if (StringUtils.isNotEmpty(languageName)) {
                    doubanTv.setLanguage(String.format("|%s|", languageName.trim()));
                }
            }

            //上映日期
            Elements releaseTimeEl = subject.select("span[property=\"v:initialReleaseDate\"]");
            String releaseStr = releaseTimeEl.html();
            if (StringUtils.isNotEmpty(releaseStr)) {
                doubanTv.setReleasedata("|".concat(releaseStr.replaceAll("\\n", "|")).concat("|"));
            }

            //片长

            if(subject.get(0).getElementsMatchingOwnText("单集片长").size() == 0){
                doubanTv.setRuntime("");
            }else{
                String runtime = subject.get(0).getElementsMatchingOwnText("单集片长").first().nextSibling().toString();
                if (StringUtils.isNotEmpty(runtime)) {
                    doubanTv.setRuntime(String.format("|%s|", runtime));
                }
            }

            //别名
            Optional<Element> otherNameOpt = plElement.stream().filter(o -> o.html().equals("又名:")).findFirst();
            if (otherNameOpt.isPresent()) {
                Element otherNameEl = otherNameOpt.get();
                String otherName = otherNameEl.nextSibling().outerHtml();
                if (StringUtils.isNotEmpty(otherName)) {
                    doubanTv.setAka(otherName.trim());
                }
            }

            //豆瓣评分
            Elements scores = doc.select("div#interest_sectl").select("strong[property=\"v:average\"]");
            String scoreStr = scores.html();
            if (StringUtils.isNotEmpty(scoreStr)) {
                doubanTv.setRatingnum(scoreStr);
            }

            //简介
            Elements storyEl = doc.select("span[property=\"v:summary\"]");
            String story = storyEl.html();
            if (StringUtils.isNotEmpty(story)) {
                doubanTv.setMoviedesc(story.trim());
            }

            //首播
            String releasedata = subject.get(0).getElementsMatchingOwnText("首播").next().select("span").text();
            if (StringUtils.isNotEmpty(releasedata)) {
                doubanTv.setReleasedata(releasedata);
            }

            //标签
            String tag = doc.select("div.tags-body").text();
            if (StringUtils.isNotEmpty(tag)) {
                String tags = tag.replaceAll(" ", "|");
                doubanTv.setTags(tags);
            }

            //集数
            if(subject.get(0).getElementsMatchingOwnText("集数").size() == 0){
                doubanTv.setEpisodesCount("");
            }else{
                String episodes_count = subject.get(0).getElementsMatchingOwnText("集数").first().nextSibling().toString();
                if(StringUtils.isNotEmpty(episodes_count)){
                    doubanTv.setEpisodesCount(episodes_count);
                }
            }


            c = 0;
            synchronized (this) {
                successCount++;
            }
            doubanTvMapper.insertSelective(doubanTv);
        } catch (Exception e) {
            log.error("发生错误url >>> {}", doubanTv.getUrl());
            if (e instanceof BusinessException) {
                log.error("获取详单错误 >>> {}", ((BusinessException) e).getCommonError().getErrorMsg());
                if (((BusinessException) e).getCommonError().getErrorMsg().contains("HttpStatus: 404")) return;
            } else {
                log.error("获取详单错误 >>> {}", e.getMessage());
            }
            if (c++ < 10) {
                log.error("三秒后再次尝试获取详单  >>>{}<<<", c);
                Thread.sleep(3000);
                getTvSave(doubanTv);
            } else {
                c = 0;
                err++;
                doubanTv = null;
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        JobDoubanTvDetail jobDoubanTvDetail = new JobDoubanTvDetail();
        DoubanTv doubanTv = new DoubanTv();
        doubanTv.setUrl("https://movie.douban.com/subject/30470509/");
        jobDoubanTvDetail.getTvSave(doubanTv);
    }


}
