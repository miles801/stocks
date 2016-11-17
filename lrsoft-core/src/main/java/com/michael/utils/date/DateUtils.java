package com.michael.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author miles
 * @datetime 2014/3/21 13:16
 * 默认情况下所有的时间都会去掉毫秒数
 */
public class DateUtils {
    public static final String[] DATE_FORMAT = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS", "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmmssSSS",
            "HH:mm", "HH:mm:ss"};

    /**
     * 获得当前时间的凌晨0点
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return c.getTime();
    }

    /**
     * 获得当前时间的下一天的凌晨0点
     *
     * @param date
     * @return
     */
    public static Date getNextDayBegin(Date date) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 24, 0, 0);
        return c.getTime();
    }

    /**
     * 获得一个指定年月日，时间为0:0:0的时间对象
     * 注意：月份是从0开始的
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar c = get(new Date());
        c.set(year, month, dayOfMonth, 0, 0, 0);
        return c.getTime();
    }

    /**
     * 获得日期为当前年月日，时间为指定时间的时间对象
     *
     * @return
     */
    public static Date getDate(Date date, int hour, int minute, int seconds) {
        Calendar c = get(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), hour, minute, seconds);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    /**
     * 根据时间获得指定的Calendar并去掉毫秒数
     *
     * @param date
     * @return
     */
    public static Calendar get(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null!");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    /**
     * 获取星期
     *
     * @param date 如果日期为空，则默认为当天
     * @return
     */
    public static int getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前日期是几年的第几周
     *
     * @param date 如果日期为空，则默认为当天
     * @return
     */
    public static int getYearWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获得两个时间的时间间隔
     *
     * @param start
     * @param end
     * @return
     */
    public static long duration(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("开始时间和结束时间不能为空!");
        }
        return Math.abs(start.getTime() - end.getTime());
    }

    /**
     * 使用内置模板将字符串转换成时间
     * 如果能够正确识别则返回正确的时间对象
     * 如果解析失败，则返回null
     *
     * @param str 要解析的字符串
     * @return 时间 or null
     */
    public static Date parse(String str) {
        return parse(str, DATE_FORMAT);
    }

    /**
     * 将字符串转换成时间
     * 如果能够正确识别则返回正确的时间对象
     * 如果解析失败，则返回null
     *
     * @param str      要解析的字符串
     * @param patterns 可以指定的模板
     * @return 时间 or null
     */
    public static Date parse(String str, String... patterns) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        // 时间戳类型
        if (str.matches("\\d+") && str.length() == 13) {
            return new Date(Long.parseLong(str));
        }
        if (patterns == null || patterns.length == 0) {
            patterns = DATE_FORMAT;
        }
        for (String pattern : patterns) {
            sdf.applyPattern(pattern);
            try {
                Date d = sdf.parse(str);
                return d;
            } catch (ParseException e) {
            }
        }
        return null;
    }

}
