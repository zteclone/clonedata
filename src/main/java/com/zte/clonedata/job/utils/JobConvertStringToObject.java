package com.zte.clonedata.job.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.model.DoubanModel;
import com.zte.clonedata.job.model.HttpType;
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

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * ProjectName: clonedata-com.zte.clonedata.job.utils
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 11:26 2020/7/21
 * @Description:
 *
 *  1. Http请求获取返回值
 *  2. 从返回值取数据
 *  3. 判断是否存在 （只判断，不做db写操作）
 */
@Component
public class JobConvertStringToObject {

    @Autowired
    protected MvMapper mvMapper;
    @Autowired
    private DoubanTvMapper doubanTvMapper;
    @Autowired
    protected RunningContanst runningContanst;

    public <T> void getDoubanMv(String url, PicDownUtils picDownUtils, Map<String, T> doubanMvMap) throws BusinessException, InterruptedException {
        String result = JobHttpUtils.getHtmlData(url, 0, JobContanst.DOUBAN_HOST1, HttpType.DOUBAN_MY, true);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.get("msg") != null) throw new BusinessException(EmBusinessError.HTTP_RESULT_IPLOCK);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.size() == 0) throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
        List<DoubanModel> doubanModelList = data.toJavaList(DoubanModel.class);
        for (DoubanModel doubanModel : doubanModelList) {
            String path = getDoubanImagePath(doubanModel.getCover());
            String dbNum = mvMapper.getNumByPrimaryKey(doubanModel.getId(),RunningContanst.TYPE_DOUBAN_ID);
            if (StringUtils.isBlank(dbNum)) {
                Mv m = Mv.builder()
                        .movieid(doubanModel.getId())
                        .mvTypeid(RunningContanst.TYPE_DOUBAN_ID)
                        .url(doubanModel.getUrl())
                        .httpImage(doubanModel.getCover())
                        .filepath(path)
                        .ratingnum(doubanModel.getRate())
                        .moviename(doubanModel.getTitle())
                        .build();
                //不存在
                doubanMvMap.put(m.getMovieid(), (T) m);
            } else {
                if (!dbNum.equals(doubanModel.getRate())) {
                    mvMapper.updateRatingByPrimarKey(doubanModel.getRate(),doubanModel.getId(),RunningContanst.TYPE_DOUBAN_ID);
                }
            }

            existsFileAndAddTask(path,doubanModel.getCover(),picDownUtils);
        }
    }

    public <T> void getDoubanTv(String url, PicDownUtils picDownUtils, Map<String, T> dataMap) throws BusinessException, InterruptedException {
        String result = JobHttpUtils.getHtmlData(url, 0, JobContanst.DOUBAN_HOST1, HttpType.DOUBAN_TY, true);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.get("msg") != null) throw new BusinessException(EmBusinessError.HTTP_RESULT_IPLOCK);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data.size() == 0) throw new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
        List<DoubanModel> doubanModelList = data.toJavaList(DoubanModel.class);
        for (DoubanModel doubanModel : doubanModelList) {
            String path = getDoubanImagePath(doubanModel.getCover());
            DoubanTv tv = doubanTvMapper.selectByPrimaryKey(doubanModel.getId());
            if (tv == null) {
                //不存在
                DoubanTv doubanTv =DoubanTv.builder()
                        .url(doubanModel.getUrl())
                        .httpImage(doubanModel.getCover())
                        .filepath(path)
                        .tvid(doubanModel.getId())
                        .ratingnum(doubanModel.getRate())
                        .tvname(doubanModel.getTitle())
                        .build();
                dataMap.put(doubanModel.getId(), (T) doubanTv);
            } else {
                if (!tv.getRatingnum().equals(doubanModel.getRate())) {
                    //分数不同
                    doubanTvMapper.updateByPrimaryKeySelective(
                            DoubanTv.builder()
                                    .tvid(doubanModel.getId())
                                    .ratingnum(doubanModel.getRate())
                                    .build()
                    );
                }
            }

            existsFileAndAddTask(path,doubanModel.getCover(),picDownUtils);
        }
    }
    public <T> void getmaoyanMv(String url, PicDownUtils picDownUtils, Map<String, T> maoyanMvMap) throws BusinessException, InterruptedException {
        String result = JobHttpUtils.getHtmlData(url, 0, JobContanst.MAOYAN_HOST1, HttpType.MAOYAN_MV, true);
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
                    String path = runningContanst.BASEURL.concat("images").concat(File.separator).concat(JobContanst.TYPE_MAOYAN).concat(File.separator).concat(id).concat(".jpg");
                    String dbnNum = mvMapper.getNumByPrimaryKey(id,RunningContanst.TYPE_MAOYAN_ID);
                    if (StringUtils.isBlank(dbnNum)) {
                        //不存在
                        Mv m = Mv.builder().movieid(id).mvTypeid(RunningContanst.TYPE_MAOYAN_ID)
                                .url(JobContanst.MAOYAN_HTTPS.concat(mvUrl))
                                .httpImage(img)
                                .filepath(path)
                                .ratingnum(num2)
                                .moviename(name2)
                                .build();
                        maoyanMvMap.put(m.getMovieid(), (T) m);
                    } else {
                        if (!dbnNum.equals(num2)) {
                            mvMapper.updateRatingByPrimarKey(num2,id,RunningContanst.TYPE_MAOYAN_ID);
                        }
                    }
                    existsFileAndAddTask(path,img,picDownUtils);
                }
            }
        }else {
            throw  new BusinessException(EmBusinessError.HTTP_RESULT_NULL);
        }

    }
    /**
     * 1. 存在   不处理
     * 2. 不存在 加入任务
     */
    private void existsFileAndAddTask(String path,String imgUrl, PicDownUtils picDownUtils){
        File file = new File(path);
        if (! file.exists()) {
            picDownUtils.urls.add(imgUrl);
            picDownUtils.files.add(file);
        }
    }
    private String getDoubanImagePath(String imageurl) {
        return getImagePath(imageurl, JobContanst.TYPE_DOUBAN);
    }
    private String getImagePath(String imageurl,String type) {
        String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
        return runningContanst.BASEURL.concat("images").concat(File.separator).concat(type).concat(File.separator).concat(name);
    }


}
