package com.zte.clonedata.contanst;

import com.zte.clonedata.util.DateUtils;

/**
 * ProjectName: clonedata-com.zte.clonedata
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 16:41 2020/5/28
 * @Description:
 */
public class JobContanst {

    public static final String CHARSET = "utf-8";

    public static final String DOUBAN = "豆瓣";
    public static final String MAOYAN = "猫眼";
    public static final String TYPE_DOUBAN = "douban";
    public static final String TYPE_MAOYAN = "maoyan";
    public static final String DOUBAN_HOST1 = "movie.douban.com";
    public static final String DOUBAN_HOST_SEARCH = "search.douban.com";
    public static final String MAOYAN_HOST1 = "maoyan.com";
    public static final String[] DOUBAN_COUNTRYS = {
            "中国大陆","美国","中国香港",
            "中国台湾","日本", "韩国",
            "英国","法国","德国",
            "意大利", "西班牙","印度",
            "泰国","俄罗斯","伊朗",
            "加拿大","澳大利亚","爱尔兰",
            "瑞典","巴西","丹麦"};
    public static final int DOUBAN_COUNTRYS_LEN = DOUBAN_COUNTRYS.length;
    public static final String[] years = {
            "2020","2019","2018",
            "2017","2016","2015",
            "2014","2013","2012",
            "2011","2010","2009",
            "2000","1990","1999",
            "1980","1989","1970",
            "1979","1960","1969",
            "1"};
    public static final int yearsLen = years.length;

    public static final String MAOYAN_HTTPS = "https://maoyan.com";

    public static final String NULL_NUM = "暂无评分";

    public static final int DOUBAN_MAX_THUS_PAGE = 300;
    public static final int DOUBAN_MAX_PAGE = 2000;
    public static final int MAOYAN_MAX_THUS_PAGE = 100;
    public static final int MAOYAN_MAX_PAGE = 1000;
}
