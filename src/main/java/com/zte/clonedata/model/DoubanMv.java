package com.zte.clonedata.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Getter
@ToString
public class DoubanMv implements Serializable {
    private String movieid;
    private String moviename;
    private String director;
    private String scenarist;
    private String actors;
    private String type;
    private String country;
    private String language;
    private String releasedata;
    private String runtime;
    private String ratingnum;
    private String tags;
    private String moviedesc;
    private String pDate;
    private String aka;
    private String httpImage;
    private String filepath;
    private String url;

    private static final long serialVersionUID = 1L;


    public void setHttpImage(String httpImage) {
        this.httpImage = httpImage;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public void setDirector(String director) {
        if (StringUtils.isEmpty(director)) return;
        if (StringUtils.isEmpty(this.director)){
            this.director = director;
        }else {
            this.director = this.director.concat(",").concat(director);
        }
    }

    public void setScenarist(String scenarist) {
        if (StringUtils.isEmpty(scenarist)) return;
        if (StringUtils.isEmpty(this.scenarist)){
            this.scenarist = scenarist;
        }else {
            this.scenarist = this.scenarist.concat(",").concat(scenarist);
        }
    }

    public void setActors(String actors) {
        if (StringUtils.isEmpty(actors)) return;
        if (StringUtils.isEmpty(this.actors)){
            this.actors = actors;
        }else {
            this.actors = this.actors.concat(",").concat(actors);
        }
    }

    public void setType(String type) {
        if (StringUtils.isEmpty(type)) return;
        if (StringUtils.isEmpty(this.type)){
            this.type = type;
        }else {
            this.type = this.type.concat(",").concat(type);
        }
    }

    public void setCountry(String country) {
        if (StringUtils.isEmpty(country)) return;
        if (StringUtils.isEmpty(this.country)){
            this.country = country;
        }else {
            this.country = this.country.concat(",").concat(country);
        }
    }

    public void setLanguage(String language) {
        if (StringUtils.isEmpty(language)) return;
        if (StringUtils.isEmpty(this.language)){
            this.language = language;
        }else {
            this.language = this.language.concat(",").concat(language);
        }
    }

    public void setReleasedata(String releasedata) {
        if (StringUtils.isEmpty(releasedata)) return;
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
        if (StringUtils.isEmpty(tags)) return;
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
        if (StringUtils.isEmpty(aka)) return;
        if (StringUtils.isEmpty(this.aka)){
            this.aka = aka;
        }else {
            this.aka = this.aka.concat(",").concat(aka);
        }
    }
}