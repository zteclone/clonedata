package com.zte.clonedata.util;

import com.zte.clonedata.model.error.BusinessException;
import com.zte.clonedata.model.error.EmBusinessError;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Calendar;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Liangxiaomin
 * @Date Created in 16:30 2019/6/12
 * @Description:
 */

public class CronUtils {
    private static final Logger log = LoggerFactory.getLogger(CronUtils.class);
    private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

    public CronUtils() {
    }

    public static String getCron(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
        String formatTimeStr = "";
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }

        return formatTimeStr;
    }

    public static Date getDate(String cron) {
        if (cron == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
            Date date = null;

            try {
                date = sdf.parse(cron);
            } catch (ParseException var4) {
                log.error("cron转换日期异常：" + var4.getMessage());
            }

            return date;
        }
    }

    public static boolean isValidExpression(String cronExpression) throws ParseException, BusinessException {
        if (StringUtils.isBlank(cronExpression)){
            throw new BusinessException(EmBusinessError.JOB_CRON_IS_NULL);
        }
        CronTriggerImpl trigger = new CronTriggerImpl();

        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime((Calendar)null);
            return date != null && date.after(new Date());
        } catch (Exception var3) {
            log.error(var3.getMessage());
            return false;
        }
    }

    public static void main(String[] args) throws ParseException, BusinessException {
        Date date = new Date(1592294746000L);
        SimpleDateFormat s = new SimpleDateFormat("yyyy MM dd HH mm ss");
        System.out.println(s.format(date));
        System.out.println(new Date().getTime());
    }
}