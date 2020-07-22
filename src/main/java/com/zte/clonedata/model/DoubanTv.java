package com.zte.clonedata.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Getter
public class DoubanTv implements Serializable {
    @ExcelProperty(value = "编号",index = 0)
    private String tvid;
    @ExcelProperty(value = "名称",index = 1)
    private String tvname;
    @ExcelProperty(value = "导演",index = 4)
    private String director;
    @ExcelProperty(value = "编剧",index = 5)
    private String scenarist;
    @ExcelProperty(value = "演员",index = 3)
    private String actors;
    @ExcelProperty(value = "类型",index = 6)
    private String type;
    @ExcelProperty(value = "国家",index = 7)
    private String country;
    @ExcelProperty(value = "语言",index = 8)
    private String language;
    @ExcelProperty(value = "上映时间",index = 9)
    private String releasedata;
    @ExcelProperty(value = "时长",index = 10)
    private String runtime;
    @ExcelProperty(value = "评分",index = 11)
    private String ratingnum;
    @ExcelProperty(value = "标签",index = 12)
    private String tags;
    @ExcelProperty(value = "描述",index = 2)
    private String moviedesc;
    @ExcelIgnore
    private String pDate;
    @ExcelProperty(value = "别名",index = 13)
    private String aka;
    @ExcelProperty(value = "封面地址",index = 14)
    private String httpImage;
    @ExcelProperty(value = "封面本机地址",index = 15)
    private String filepath;
    @ExcelProperty(value = "详情页地址",index = 16)
    private String url;
    @ExcelProperty(value = "集数",index = 17)
    private String episodesCount;
    private static final long serialVersionUID = 1L;


    public void setTvid(String tvid) {
        this.tvid = tvid;
    }

    public void setTvname(String tvname) {
        this.tvname = tvname;
    }

    public void setHttpImage(String httpImage) {
        this.httpImage = httpImage;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setEpisodesCount(String episodesCount) {
        this.episodesCount = episodesCount;
    }

    public void setDirector(String director) {
        if (StringUtils.isEmpty(this.director)){
            this.director = director;
        }else {
            this.director = this.director.concat(",").concat(director);
        }
    }

    public void setScenarist(String scenarist) {
        if (StringUtils.isEmpty(this.scenarist)){
            this.scenarist = scenarist;
        }else {
            this.scenarist = this.scenarist.concat(",").concat(scenarist);
        }
    }

    public void setActors(String actors) {
        if (StringUtils.isEmpty(this.actors)){
            this.actors = actors;
        }else {
            this.actors = this.actors.concat(",").concat(actors);
        }
    }

    public void setType(String type) {
        if (StringUtils.isEmpty(this.type)){
            this.type = type;
        }else {
            this.type = this.type.concat(",").concat(type);
        }
    }

    public void setCountry(String country) {
        if (StringUtils.isEmpty(this.country)){
            this.country = country;
        }else {
            this.country = this.country.concat(",").concat(country);
        }
    }

    public void setLanguage(String language) {
        if (StringUtils.isEmpty(this.language)){
            this.language = language;
        }else {
            this.language = this.language.concat(",").concat(language);
        }
    }

    public void setReleasedata(String releasedata) {
        if (StringUtils.isEmpty(this.releasedata)){
            this.releasedata = releasedata;
        }else {
            this.releasedata = this.releasedata.concat(",").concat(releasedata);
        }
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setRatingnum(String ratingnum) {
        this.ratingnum = ratingnum;
    }

    public void setTags(String tags) {
        if (StringUtils.isEmpty(this.tags)){
            this.tags = tags;
        }else {
            this.tags = this.tags.concat(",").concat(tags);
        }
    }

    public void setMoviedesc(String moviedesc) {
        this.moviedesc = moviedesc;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public void setAka(String aka) {
        if (StringUtils.isEmpty(this.aka)){
            this.aka = aka;
        }else {
            this.aka = this.aka.concat(",").concat(aka);
        }
    }
}