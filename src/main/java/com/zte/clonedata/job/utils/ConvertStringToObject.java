package com.zte.clonedata.job.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.Contanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.model.DoubanModel;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import com.zte.clonedata.util.PicDownUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.utils
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 11:26 2020/7/21
 * @Description:
 */
@Component
public class ConvertStringToObject {

    @Autowired
    protected MvMapper mvMapper;
    @Autowired
    private DoubanTvMapper doubanTvMapper;
    @Autowired
    protected RunningContanst runningContanst;

    public <T> void getDoubanMv(String result, PicDownUtils picDownUtils, Map<String, T> doubanMvMap) throws BusinessException {
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.get("msg") != null) throw new BusinessException(EmBusinessError.HTTP_RESULT_IPLOCK);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.size() == 0) throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
        List<DoubanModel> doubanModelList = data.toJavaList(DoubanModel.class);
        for (DoubanModel doubanModel : doubanModelList) {
            String imageurl = doubanModel.getCover();
            String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
            String path = runningContanst.BASEURL.concat("images").concat(File.separator).concat(Contanst.TYPE_DOUBAN).concat(File.separator).concat(name);
            Mv m = new Mv();
            m.setMovieid(doubanModel.getId());
            m.setMvTypeid(RunningContanst.TYPE_DOUBAN_ID);
            Mv mv = mvMapper.selectByPrimaryKey(m);
            if (mv == null) {
                //不存在
                m.setUrl(doubanModel.getUrl());
                m.setHttpImage(imageurl);
                m.setFilepath(path);
                m.setMovieid(doubanModel.getId());
                m.setRatingnum(doubanModel.getRate());
                m.setMoviename(doubanModel.getTitle());
                doubanMvMap.put(m.getMovieid(), (T) m);
            } else {
                if (!mv.getRatingnum().equals(doubanModel.getRate())) {
                    updateMvNum(m,doubanModel.getRate());
                }
            }

            existsFile(path,imageurl,picDownUtils);
        }
    }
    public <T> void getDoubanTv(String result, PicDownUtils picDownUtils, Map<String, T> dataMap) throws BusinessException {
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.get("msg") != null) throw new BusinessException(EmBusinessError.HTTP_RESULT_IPLOCK);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.size() == 0) throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
        List<DoubanModel> doubanModelList = data.toJavaList(DoubanModel.class);
        for (DoubanModel doubanModel : doubanModelList) {
            String imageurl = doubanModel.getCover();
            String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
            String path = runningContanst.BASEURL.concat("images").concat(File.separator).concat(Contanst.TYPE_DOUBAN).concat(File.separator).concat(name);
            DoubanTv tv = doubanTvMapper.selectByPrimaryKey(doubanModel.getId());
            if (tv == null) {
                //不存在
                DoubanTv doubanTv = new DoubanTv();
                doubanTv.setUrl(doubanModel.getUrl());
                doubanTv.setHttpImage(imageurl);
                doubanTv.setFilepath(path);
                doubanTv.setTvid(doubanModel.getId());
                doubanTv.setRatingnum(doubanModel.getRate());
                doubanTv.setTvname(doubanModel.getTitle());
                dataMap.put(doubanModel.getId(), (T) doubanTv);
            } else {
                if (!tv.getRatingnum().equals(doubanModel.getRate())) {
                    //分数不同
                    DoubanTv doubanTv = new DoubanTv();
                    doubanTv.setTvid(doubanModel.getId());
                    doubanTv.setRatingnum(doubanModel.getRate());
                    doubanTvMapper.updateByPrimaryKeySelective(doubanTv);
                }
            }

            existsFile(path,imageurl,picDownUtils);
        }
    }
    public <T> void getmaoyanMv(String result, PicDownUtils picDownUtils, Map<String, T> maoyanMvMap) throws BusinessException {
        if (StringUtils.isBlank(result)) throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
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
                    String path = runningContanst.BASEURL.concat("images").concat(File.separator).concat(Contanst.TYPE_MAOYAN).concat(File.separator).concat(id).concat(".jpg");
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
                            updateMvNum(m,num2);
                        }
                    }
                    existsFile(path,img,picDownUtils);
                }
            }
        }else {
            throw  new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
        }

    }

    private void existsFile(String path,String imgUrl, PicDownUtils picDownUtils){
        /**
         * 1. 存在   不处理
         * 2. 不存在 加入任务
         */
        File file = new File(path);
        if (! file.exists()) {
            picDownUtils.urls.add(imgUrl);
            picDownUtils.files.add(file);
        }
    }

    private void updateMvNum(Mv mv,String num){
        mv.setRatingnum(num);
        mvMapper.updateByPrimaryKeySelective(mv);
    }
}
