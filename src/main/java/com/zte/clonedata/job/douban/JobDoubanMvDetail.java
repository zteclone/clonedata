package com.zte.clonedata.job.douban;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.dao.DoubanMvMapper;
import com.zte.clonedata.model.DoubanMv;
import com.zte.clonedata.util.HttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.douban
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 13:58 2020/6/4
 * @Description:
 */
@Slf4j
public class JobDoubanMvDetail extends Thread {

    private List<DoubanMv> list;
    private DoubanMvMapper doubanMvMapper;

    public JobDoubanMvDetail(List<DoubanMv> list, DoubanMvMapper doubanMvMapper) {
        this.doubanMvMapper = doubanMvMapper;
        this.list = list;
    }

    @SneakyThrows
    @Override
    public void run() {
        for (DoubanMv mv : list) {
            getMovice(mv);
            doubanMvMapper.insertSelective(mv);
        }
    }
    private int c = 0;
    private void getMovice(DoubanMv doubanMv) throws InterruptedException {
        String url = "https://douban.uieee.com/v2/movie/subject/".concat(doubanMv.getMovieid());
        try {
            String result = HttpUtils.getJson(url, Contanst.DOUBAN_HOST2);
            JSONObject json = JSONObject.parseObject(result);
            // 导演
            JSONArray directors = json.getJSONArray("directors");
            if (directors != null) {
                for (int i = 0; i < directors.size(); i++) {
                    JSONObject jsonObject = directors.getJSONObject(i);
                    doubanMv.setDirector(jsonObject.get("name").toString());
                }
            }

            //编剧
            JSONArray writers = json.getJSONArray("writers");
            if (writers != null) {
                for (int i = 0; i < writers.size(); i++) {
                    JSONObject jsonObject = writers.getJSONObject(i);
                    doubanMv.setScenarist(jsonObject.get("name").toString());
                }
            }
            //主演
            JSONArray casts = json.getJSONArray("casts");
            if (casts != null) {
                for (int i = 0; i < casts.size(); i++) {
                    JSONObject jsonObject = casts.getJSONObject(i);
                    doubanMv.setActors(jsonObject.get("name").toString());
                }
            }
            //类型
            JSONArray genres = json.getJSONArray("genres");
            if (genres != null) {
                for (int i = 0; i < genres.size(); i++) {
                    doubanMv.setType(genres.getString(i));
                }
            }
            //制片国家/地区
            JSONArray countries = json.getJSONArray("countries");
            if (countries != null) {
                for (int i = 0; i < countries.size(); i++) {
                    doubanMv.setCountry(countries.getString(i));
                }
            }
            //语言
            JSONArray languages = json.getJSONArray("languages");
            if (languages != null) {
                for (int i = 0; i < languages.size(); i++) {
                    doubanMv.setLanguage(languages.getString(i));
                }
            }
            //上映日期
            JSONArray pubdates = json.getJSONArray("pubdates");
            if (pubdates != null) {
                for (int i = 0; i < pubdates.size(); i++) {
                    doubanMv.setReleasedata(pubdates.getString(i));
                }
            }
            //片长
            JSONArray durations = json.getJSONArray("durations");
            if (durations != null) {
                for (int i = 0; i < durations.size(); i++) {
                    doubanMv.setRuntime(durations.getString(i));
                }
            }
            //豆瓣评分
            JSONObject rating = json.getJSONObject("rating");
            doubanMv.setRatingnum(rating.getBigDecimal("average").toString());
            //标签
            JSONArray tags = json.getJSONArray("tags");
            if (tags != null) {
                for (int i = 0; i < tags.size(); i++) {
                    doubanMv.setTags(tags.getString(i));
                }
            }
            //简介
            doubanMv.setMoviedesc(json.getString("summary"));
            //又名
            JSONArray aka = json.getJSONArray("aka");
            if (aka != null) {
                for (int i = 0; i < aka.size(); i++) {
                    doubanMv.setAka(aka.getString(i));
                }
            }
            c = 0;
        } catch (Exception e) {
            log.error("发生错误url >>> {}", url);
            log.error("获取详单错误 >>> {}", e.getMessage());
            if (c++ < 10) {
                log.error("三秒后再次尝试获取详单  >>>{}<<<", c);
                Thread.sleep(3000);
                getMovice(doubanMv);
            } else {
                c = 0;
            }
        }
    }
}
