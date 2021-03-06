package com.zte.clonedata.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ProjectName: clonedata-com.zte.clonedata.util
 *
 * @Author: Liang Xiaomin
 * @Date: Creating in 9:36 2020/5/29
 * @Description:
 */
public class DateUtils {
    private DateUtils(){}

    public static ThreadLocal<SimpleDateFormat> sdf1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static ThreadLocal<SimpleDateFormat> sdf2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };
    public static ThreadLocal<SimpleDateFormat> sdf3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    public static ThreadLocal<SimpleDateFormat> sdf4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };
    public static String getNowYYYYMMDD(){
        return sdf1.get().format(new Date());
    }

    public static String getNowYYYYMMDDHHMMSS(){
        return sdf2.get().format(new Date());
    }

    public static String getString(Date date){
        return sdf3.get().format(date);
    }

    public static Date getFewDate(int i){
        Date date = new Date();
        if (i == 0) return date;
        return new Date(date.getTime() + (24*60*60*1000*i));
    }
    public static String getFewDay(int i){
        Date fewDate = getFewDate(i);
        return sdf4.get().format(fewDate);
    }
}
