package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanMvMapper;
import com.zte.clonedata.model.DoubanMv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.HttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static int successCount = 0;

    private List<DoubanMv> list;
    private DoubanMvMapper doubanMvMapper;

    public JobDoubanMvDetail(List<DoubanMv> list, DoubanMvMapper doubanMvMapper) {
        this.doubanMvMapper = doubanMvMapper;
        this.list = list;
    }

    @SneakyThrows
    @Override
    public void run() {
        String date = DateUtils.getNowYYYYMMDDHHMMSS();
        for (DoubanMv mv : list) {
            mv.setpDate(date);
            getMoviceSave(mv);
        }
    }


    private int c = 0;
    private int err = 0;

    private void getMoviceSave(DoubanMv doubanMv) throws InterruptedException {
        try {
            String result = HttpUtils.getJson(doubanMv.getUrl(), Contanst.DOUBAN_HOST1);
            Document doc = Jsoup.parse(result);
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
            if (StringUtils.isNotEmpty(kindName)) {
                doubanMv.setType(String.format("|%s|", kindName.trim()));
            }

            //制片国家/地区
            Optional<Element> areaOpt = plElement.stream().filter(o -> o.html().equals("制片国家/地区:")).findFirst();
            if (areaOpt.isPresent()) {
                Element areaEl = areaOpt.get();
                String areaName = areaEl.nextSibling().outerHtml();
                if (StringUtils.isNotEmpty(areaName)) {
                    doubanMv.setCountry(String.format("|%s|", areaName.trim()));
                }
            }

            //语言
            Optional<Element> languageOpt = plElement.stream().filter(o -> o.html().equals("语言:")).findFirst();
            if (languageOpt.isPresent()) {
                Element languageEl = languageOpt.get();
                String languageName = languageEl.nextSibling().outerHtml();
                if (StringUtils.isNotEmpty(languageName)) {
                    doubanMv.setLanguage(String.format("|%s|", languageName.trim()));
                }
            }

            //上映日期
            Elements releaseTimeEl = subject.select("span[property=\"v:initialReleaseDate\"]");
            String releaseStr = releaseTimeEl.html();
            if (StringUtils.isNotEmpty(releaseStr)) {
                doubanMv.setReleasedata("|".concat(releaseStr.replaceAll("\\n","|")).concat("|"));
            }

            //片长
            Elements runtime = subject.select("span[property=\"v:runtime\"]");
            String runtimeStr = runtime.html();
            if (StringUtils.isNotEmpty(runtimeStr)) {
                doubanMv.setRuntime(String.format("|%s|",runtimeStr));
            }

            //集数 totalNumber
        /*Optional<Element> totalNumberOpt = plElement.stream().filter(o -> o.html().equals("集数:")).findFirst();
        if (totalNumberOpt.isPresent()) {
            Element totalNumberEl = totalNumberOpt.get();
            String totalNumber = totalNumberEl.nextSibling().outerHtml();
            if (StringUtils.isNotEmpty(totalNumber)) {
                doubanMv.setTotalNumber(Integer.parseInt(totalNumber.trim()));
            }
        }
*/

            //别名
            Optional<Element> otherNameOpt = plElement.stream().filter(o -> o.html().equals("又名:")).findFirst();
            if (otherNameOpt.isPresent()) {
                Element otherNameEl = otherNameOpt.get();
                String otherName = otherNameEl.nextSibling().outerHtml();
                if (StringUtils.isNotEmpty(otherName)) {
                    doubanMv.setAka(otherName.trim());
                }
            }

            //豆瓣评分
            Elements scores = doc.select("div#interest_sectl").select("strong[property=\"v:average\"]");
            String scoreStr = scores.html();
            if (StringUtils.isNotEmpty(scoreStr)) {
                doubanMv.setRatingnum(scoreStr);
            }

            //简介
            Elements storyEl = doc.select("span[property=\"v:summary\"]");
            String story = storyEl.html();
            if (StringUtils.isNotEmpty(story)) {
                doubanMv.setMoviedesc(story.trim());
            }

            c = 0;
            synchronized (this){
                successCount ++;
            }
            doubanMvMapper.insertSelective(doubanMv);
        } catch (Exception e) {
            log.error("发生错误url >>> {}", doubanMv.getUrl());
            if (e instanceof BusinessException){
                log.error("获取详单错误 >>> {}", ((BusinessException) e).getCommonError().getErrorMsg());
                if (((BusinessException) e).getCommonError().getErrorMsg().contains("HttpStatus: 404")) return;
            }else {
                log.error("获取详单错误 >>> {}", e.getMessage());
            }
            if (c++ < 10) {
                log.error("三秒后再次尝试获取详单  >>>{}<<<", c);
                Thread.sleep(3000);
                getMoviceSave(doubanMv);
            } else {
                c = 0;
                err++;
                doubanMv = null;
            }
        }
    }

    public static void main(String[] args) throws BusinessException, IOException {
        String url = "https://movie.douban.com/top250?start=";
        String link = "&filter=";
        int num = 150;
        while (num < 250) {
            String uri = url + num + link;
            Document document = Jsoup.connect(uri).get();
            Elements ByTag = document.getElementsByTag("ol");
            Elements li = ByTag.select("li");
            for (int i = 0; i < li.size(); i++) {

                String text = li.get(i).select("div.pic").get(0).select("em").get(0).text();
                String view = "电影排名：" + text;
                System.out.println("电影排名：" + view);
                String hrefAddr = li.get(i).select("div.pic").get(0).select("a").attr("href");
                System.out.println("电影连接：" + hrefAddr);

                //========================*详情页面爬取开始*=========================
                Document docum = Jsoup.connect(hrefAddr).get();
                String daoyan = docum.select("div#info").select("span.attrs").get(0).text();
                System.out.println("导演：" + daoyan);
                String jieshao = docum.select("div#link-report").select("span[property=v:summary]").get(0).text();
                System.out.println("剧情介绍：" + jieshao);
                //========================*详情页面爬取结束*=========================
                String imgurl = li.get(i).select("a").get(0).select("img").attr("src");
                String imgalt = li.get(i).select("a").get(0).select("img").attr("alt");
                System.out.println("电影名称：" + imgalt);
                System.out.println("图片地址：" + imgurl);
                Elements hd = li.get(i).select("div.hd");
                Element title = hd.select("a[href]").get(0);
                String titleContent = title.text();
                String titleView = "电影标题：" + titleContent;
                System.out.println(titleView);

                Element bd = li.get(i).select("div.bd").get(0);
                Elements pContext = bd.select("p");
                Element selectFirst = pContext.first();
                String fristP = selectFirst.text();
                String repon = "电影演员：" + fristP;
                System.out.println(repon);
                Elements star = bd.select("div.star");
                String score = star.select("span.rating_num").text();
                String pinfen = "电影评分：" + score;
                System.out.println(pinfen);
                String lastSpan = star.select("span").last().text();
                String count = "评价人数：" + lastSpan;
                System.out.println(count);
                Elements quote = bd.select("p.quote");
                String mothod = quote.select("span.inq").text();
                String miaoshu = "电影描述：" + mothod + "\n\n";
                System.out.println(miaoshu);
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
            }
            num += 25;
        }
    }
}
