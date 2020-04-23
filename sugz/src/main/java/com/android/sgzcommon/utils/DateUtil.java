package com.android.sgzcommon.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sgz on 2016/a12/20.
 */

public class DateUtil {
    /**
     * 获取当前时间戳的字符串
     *
     * @return
     */
    public static Date getCurrentYMDHMDate() {
        String time = getCurrentYMDDateStr() + " " + getCurrentHMDateStr();
        return getYMDHMDate(time);
    }

    /**
     * 获取当前时间戳的字符串
     *
     * @param hour 格式为：HH:mm的时间字符串
     * @return
     */
    public static Date getCurrentYMDHMDate(String hour) {
        String time = getCurrentYMDDateStr() + " " + hour;
        return getYMDHMDate(time);
    }

    public static Date getCurrentYMDDate() {
        String time = getCurrentYMDDateStr();
        return getYMDDate(time);
    }

    /**
     * @param time 格式为：yyyy-MM-dd的时间字符串
     * @return
     */
    public static Date getYMDDate(String time) {
        return getDate(time, "yyyy-MM-dd");
    }

    /**
     * @param time 格式为：yyyy-MM-dd HH:mm的时间字符串
     * @return
     */
    public static Date getYMDHMDate(String time) {
        return getDate(time, "yyyy-MM-dd HH:mm");
    }

    /**
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间字符串
     * @return
     */
    public static Date getYMDHMSDate(String time) {
        return getDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDate(String time, String format) {
        Date date = null;
        SimpleDateFormat f = new SimpleDateFormat(format);
        if (!TextUtils.isEmpty(time)) {
            try {
                date = f.parse(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date getDate(long time) {
        return new Date(time);
    }

    /**
     * 获取当前日期，格式：HH:mm
     *
     * @return
     */
    public static String getCurrentHMDateStr() {
        return getHMDateStr(System.currentTimeMillis());
    }


    /**
     * 获取时：分时间，格式：HH:mm
     *
     * @return
     */
    public static String getHMDateStr(long time) {
        return getDateStr(time, "HH:mm");
    }

    /**
     * 获取当前日期，格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentYMDDateStr() {
        return getDateStr(System.currentTimeMillis(), "yyyy-MM-dd");
    }

    /**
     * 获取当前日期，格式：yyyy-MM
     *
     * @return
     */
    public static String getCurrentYMDateStr() {
        return getDateStr(System.currentTimeMillis(), "yyyy-MM");
    }

    /**
     * 获取当前日期，格式：MM月dd日
     *
     * @return
     */
    public static String getCurrentMDDateStr() {
        return getDateStr(System.currentTimeMillis(), "MM-dd");
    }

    /**
     * 获取当前日期，格式：MM月dd日
     *
     * @return
     */
    public static String getCurrentMDDateCZStr() {
        return getDateStr(System.currentTimeMillis(), "MM月dd日");
    }

    /**
     * 获取当前时间戳字符串
     *
     * @return
     */
    public static String getCurrntTimeMillisStr() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取当前星期几
     *
     * @return
     */
    public static String getCurrntWeekDayCZStr() {
        String dayOfWeek = "";
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int day = c.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                dayOfWeek = "星期日";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
        }
        return dayOfWeek;
    }

    /**
     * 获取当前时间，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentYMDHMSDateStr() {
        return getYMDHMSDateStr(System.currentTimeMillis());
    }


    /**
     * 获取时间字符串，格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getYMDDateStr(long time) {
        return getDateStr(time, "yyyy-MM-dd");
    }

    /**
     * 转换时间字符串，timeformat格式转换为 yyyy-MM-dd
     *
     * @param changeTime        要转换的日期
     * @param timeCurrentFormat changeTime当前的format
     * @return
     */
    public static String change2YMDDateStr(String changeTime, String timeCurrentFormat) {
        return changeDateFormatStr(changeTime, timeCurrentFormat, "yyyy-MM-dd");
    }

    /**
     * 转换时间字符串，timeformat格式转换为 yyyy-MM-dd
     *
     * @param changeTime        要转换的日期
     * @param timeCurerntFormat changeTime当前的format
     * @param changeFormat      装换后的格式
     * @return
     */
    public static String changeDateFormatStr(String changeTime, String timeCurerntFormat, String changeFormat) {
        String newDateStr = "";
        Date date = getDate(changeTime, timeCurerntFormat);
        if (date != null) {
            newDateStr = getDateStr(date.getTime(), changeFormat);
        }
        return newDateStr;
    }

    /**
     * 获取时间字符串，格式：yyyy-MM-dd HH:mm:ss
     *
     * @param time 时间
     * @return
     */
    public static String getYMDHMSDateStr(long time) {
        return getDateStr(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDateStr(long time, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date date = new Date(time);
        return f.format(date);
    }

    /**
     * 获取系统当前月的天数
     *
     * @return
     */
    public static long getDaysOfCurrentMonth() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        return getDaysOfMonth(year, month);
    }

    /**
     * 获取一个月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static long getDaysOfMonth(int year, int month) {
        int day = 1;
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        long time = c.getTimeInMillis();
        c.set(year, month, day);
        long time1 = c.getTimeInMillis();
        long days = Math.abs(time1 - time) / (1000 * 60 * 60 * 24);
        return days;
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    /**
     * 1~a12
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
