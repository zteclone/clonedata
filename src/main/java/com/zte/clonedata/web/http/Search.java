package com.zte.clonedata.web.http;

import com.google.common.collect.Lists;
import com.zte.clonedata.contanst.JobContanst;
import com.zte.clonedata.contanst.RunningContanst;
import com.zte.clonedata.dao.DoubanTvMapper;
import com.zte.clonedata.dao.MvMapper;
import com.zte.clonedata.job.douban.JobDoubanMvDetail;
import com.zte.clonedata.job.douban.JobDoubanTvDetail;
import com.zte.clonedata.job.model.HttpType;
import com.zte.clonedata.job.utils.JobHttpUtils;
import com.zte.clonedata.model.DoubanTv;
import com.zte.clonedata.model.Mv;
import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.util.DateUtils;
import com.zte.clonedata.util.PicDownUtils;
import com.zte.clonedata.util.ResponseUtils;
import com.zte.clonedata.web.dto.SearchDoubanInputDTO;
import com.zte.clonedata.web.dto.SearchDoubanOutputDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: clonedata-com.zte.clonedata.web.http
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 15:37 2020/7/24
 * @Description:
 */
@Slf4j
@RequestMapping("/httpSearch")
@RestController
public class Search {

    @Autowired
    private MvMapper mvMapper;
    @Autowired
    private DoubanTvMapper doubanTvMapper;
    @Autowired
    private RunningContanst runningContanst;

    @GetMapping("/search")
    public ResponseUtils search(@RequestParam("moviename") String moviename) {
        if (StringUtils.isBlank(moviename) || StringUtils.isBlank(moviename.trim())) {
            return ResponseUtils.fail("参数为空");
        }
        moviename = moviename.trim();
        String[] str = new String[2];
        for (int i = 0; i < 2; i++) {
            try {
                str[i] = this.getDoubanWindowData(moviename, String.valueOf(i*15));
            } catch (BusinessException e) {
                log.error(e.getCommonError().getErrorMsg());
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        return ResponseUtils.successData(str);
    }

    @PostMapping("/doubanSave")
    public ResponseUtils doubanSave(@RequestBody List<SearchDoubanInputDTO> list) throws BusinessException, InterruptedException {
        JobDoubanTvDetail jobDoubanTvDetail = new JobDoubanTvDetail();
        JobDoubanMvDetail jobDoubanMvDetail = new JobDoubanMvDetail();
        PicDownUtils picDownUtils = new PicDownUtils();

        LinkedList<SearchDoubanOutputDTO> resultList = Lists.newLinkedList();
        String nowYYYYMMDDHHMMSS = DateUtils.getNowYYYYMMDDHHMMSS();
        for (SearchDoubanInputDTO searchDoubanDTO : list) {
            String id = searchDoubanDTO.getId();
            if (StringUtils.isBlank(id) || StringUtils.isBlank(searchDoubanDTO.getUrl())) continue;
            try {
                Mv mv = mvMapper.selectByPrimaryKey(Mv.builder().movieid(id).mvTypeid(RunningContanst.TYPE_DOUBAN_ID).build());
                SearchDoubanOutputDTO resultDTO = SearchDoubanOutputDTO.builder()
                        .id(searchDoubanDTO.getId())
                        .title(searchDoubanDTO.getTitle())
                        .build();
                if (mv == null) {
                    DoubanTv doubanTv = doubanTvMapper.selectByPrimaryKey(id);
                    if (doubanTv == null) {
                        String result = JobHttpUtils.getHtmlData(searchDoubanDTO.getUrl(), 0, JobContanst.DOUBAN_HOST1, HttpType.DETAIL, false);
                        if (StringUtils.isBlank(result)) continue;
                        String imagePath = getImagePath(searchDoubanDTO.getCover_url());
                        Document doc = Jsoup.parse(result);
                        if (result.contains("<span class=\"pl\">集数:</span>")) {
                            DoubanTv build = DoubanTv.builder().ratingnum(searchDoubanDTO.getRating())
                                    .tvid(searchDoubanDTO.getId())
                                    .tvname(searchDoubanDTO.getTitle())
                                    .httpImage(searchDoubanDTO.getCover_url())
                                    .url(searchDoubanDTO.getUrl())
                                    .filepath(imagePath)
                                    .pDate(nowYYYYMMDDHHMMSS)
                                    .build();
                            jobDoubanTvDetail.getDoubantvData(build, doc);
                            doubanTvMapper.insertSelective(build);
                            resultDTO.setProgramType("tv");
                            BeanUtils.copyProperties(build, resultDTO);
                        } else {
                            Mv build = Mv.builder().ratingnum(searchDoubanDTO.getRating())
                                    .movieid(searchDoubanDTO.getId())
                                    .mvTypeid(RunningContanst.TYPE_DOUBAN_ID)
                                    .moviename(searchDoubanDTO.getTitle())
                                    .httpImage(searchDoubanDTO.getCover_url())
                                    .url(searchDoubanDTO.getUrl())
                                    .filepath(imagePath)
                                    .pDate(nowYYYYMMDDHHMMSS)
                                    .build();
                            jobDoubanMvDetail.getDoubanmvData(build, doc);
                            mvMapper.insertSelective(build);
                            resultDTO.setProgramType("mv");
                            BeanUtils.copyProperties(build, resultDTO);
                        }
                        File file = new File(imagePath);
                        if (!file.exists()) {
                            picDownUtils.files.add(file);
                            picDownUtils.urls.add(searchDoubanDTO.getCover_url());
                        }
                    } else {
                        resultDTO.setProgramType("tv");
                        BeanUtils.copyProperties(doubanTv, resultDTO);
                    }
                } else {
                    resultDTO.setProgramType("mv");
                    BeanUtils.copyProperties(mv, resultDTO);
                }
                resultList.add(resultDTO);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
        }
        Thread thread = new Thread(picDownUtils);
        thread.start();
        return ResponseUtils.successData(resultList);
    }

    private String getImagePath(String imageurl) {
        String name = imageurl.substring(imageurl.lastIndexOf("/") + 1);
        return runningContanst.BASEURL.concat("images").concat(File.separator).concat(JobContanst.TYPE_DOUBAN).concat(File.separator).concat(name);
    }

    @GetMapping("/getDataTypeId")
    public String getDataTypeId(@RequestParam("dataTypeName") String dataTypeName) {
        if ("豆瓣".equals(dataTypeName)) {
            return RunningContanst.TYPE_DOUBAN_ID;
        } else if ("猫眼".equals(dataTypeName)) {
            return RunningContanst.TYPE_MAOYAN_ID;
        }
        return "";
    }


    private String getDoubanWindowData(String moviename, String pagestart) throws BusinessException, InterruptedException {
        //网址
        String url = String.format("https://search.douban.com/movie/subject_search?search_text=%s&cat=1002&start=%s", moviename, pagestart);
        //返回值
        String htmlData = JobHttpUtils.getHtmlData(url, 0, JobContanst.DOUBAN_HOST_SEARCH, HttpType.DETAIL, false);
        //正则获取指定内容
        Pattern pp = Pattern.compile("window\\.__DATA__ = \"([^\"]+)\"");
        Matcher m = pp.matcher(htmlData);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
}
