package com.nuu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.CHINA);
    public static final SimpleDateFormat HOUR_MIN = new SimpleDateFormat(
            "HH:mm", Locale.CHINA);
    public static final SimpleDateFormat YEAR_MONTH_DATE = new SimpleDateFormat(
            "yyyy年MM月dd日", Locale.CHINA);

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前天
     *
     * @param timeInMillis
     * @return
     */
    public static String getDate(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * 日期格式中解析日历
     *
     * @param str
     * @param sdf
     * @return
     * @throws ParseException
     */
    public static Calendar parseDate(String str, SimpleDateFormat sdf) throws ParseException {
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    /**
     * 将毫秒时间转化为 “00:00:00”格式时间
     */
    public static String getTimeFromMillisecond(long time) {

        if (time <= 0) {
            return "00:00";
        }

        int seconds = (int) (time / 1000);  //转换为秒数

        int hour = seconds / 3600;
        int minute = seconds / 60;
        int second = seconds % 60;

        if (hour >= 1) {
            return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format(Locale.CHINA, "%02d:%02d", minute, second);
        }

    }


    /**
     * 将毫秒时间转化为秒 “00”格式时间
     */
    public static String getTimeFromSecond(long time) {

        if (time <= 0) {
            return "00";
        }
        int second = (int) (time / 1000);
        String f = String.valueOf(second);
        return f;
    }

    /**
     * 套餐结束时间
     *
     * @param month 可使用多少个月
     * @return 截止日期
     */
    public static String getComboEndTime(int month) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int yearNow = calendar.get(Calendar.YEAR);
        int monthEnd = calendar.get(Calendar.MONTH) + month;
        int dayNow = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar startCal = Calendar.getInstance(Locale.CANADA);
        startCal.set(yearNow, monthEnd, dayNow);
        calendar.getTimeInMillis();
        Date date = new Date(startCal.getTimeInMillis());
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    // 获得当天24点时间
    public static int getNightTimestamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }


    /**
     * 消息内页通讯时间显示规则：
     * 1、时间段划分：00:00-06:00凌晨，06:00-12:00上午，12:00-18:00下午，18:00-00:00晚上
     * 2、今天：上午10:10、下午19:20
     * 昨天：昨天上午10:10、下午19:20
     * 前天-7天内：星期一上午10:10、星期二下午19:20
     * 7天前：2018年6月3日 下午16:23
     */
    /*public static String dateFormat(long timeStamp) {
        Date date = new Date(timeStamp);  //目标时间

        Calendar dstCal = Calendar.getInstance();
        dstCal.setTime(date);

        long curTimeMillis = System.currentTimeMillis();
        Date now = new Date(curTimeMillis);  //当前时间

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime();// 今天的开始时间

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime(); //昨天的开始时间

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date twoDayBefore = calendar.getTime(); //前天的开始时间

        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Date sevenDayBefore = calendar.getTime(); //7天前的开始时间

        String weekStr = "";
        if (date.after(sevenDayBefore) && date.before(twoDayBefore)) {  //前天-7天内
            int week = dstCal.get(Calendar.DAY_OF_WEEK);
            switch (week) {
                case Calendar.SUNDAY:
                    weekStr = "星期日";
                    break;
                case Calendar.MONDAY:
                    weekStr = "星期一";
                    break;
                case Calendar.TUESDAY:
                    weekStr = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    weekStr = "星期三";
                    break;
                case Calendar.THURSDAY:
                    weekStr = "星期四";
                    break;
                case Calendar.FRIDAY:
                    weekStr = "星期五";
                    break;
                case Calendar.SATURDAY:
                    weekStr = "星期六";
                    break;
            }


        } else if (date.after(twoDayBefore) && date.before(yesterday)) {
            weekStr = "前天";
        } else if (date.after(yesterday) && date.before(today)) {
            weekStr = "昨天";
        } else if (date.after(today)) {
            weekStr = "";
        } else {
            weekStr = YEAR_MONTH_DATE.format(date);
        }

        return weekStr + timeDescription(dstCal);
    }*/

    /**
     * 消息内页通讯时间显示规则：
     * 1、时间段划分：00:00-06:00凌晨，06:00-12:00上午，12:00-18:00下午，18:00-00:00晚上
     * 2、今天：上午10:10、下午19:20
     * 昨天：昨天上午10:10、下午19:20
     * 前天-7天内：星期一上午10:10、星期二下午19:20
     * 7天前：2018年6月3日 下午16:23
     */
    public static String dateFormat(long timeStamp) {
        Date date = new Date(timeStamp);  //目标时间

        Calendar dstCal = Calendar.getInstance();
        dstCal.setTime(date);

        long curTimeMillis = System.currentTimeMillis();
        Date now = new Date(curTimeMillis);  //当前时间

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime();// 今天的开始时间

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime(); //昨天的开始时间

        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date sevenDayBefore = calendar.getTime(); //7天前的开始时间

        String weekStr = "";
        if (date.after(sevenDayBefore) && date.before(yesterday)) {  //前天-7天内
            int week = dstCal.get(Calendar.DAY_OF_WEEK);
            switch (week) {
                case Calendar.SUNDAY:
                    weekStr = "星期日";
                    break;
                case Calendar.MONDAY:
                    weekStr = "星期一";
                    break;
                case Calendar.TUESDAY:
                    weekStr = "星期二";
                    break;
                case Calendar.WEDNESDAY:
                    weekStr = "星期三";
                    break;
                case Calendar.THURSDAY:
                    weekStr = "星期四";
                    break;
                case Calendar.FRIDAY:
                    weekStr = "星期五";
                    break;
                case Calendar.SATURDAY:
                    weekStr = "星期六";
                    break;
            }


        } else if (date.after(yesterday) && date.before(today)) {
            weekStr = "昨天";
        } else if (date.after(today)) {
            weekStr = "";
        } else {
            weekStr = YEAR_MONTH_DATE.format(date);
        }

        return weekStr + timeDescription(dstCal);
    }


    private static String timeDescription(Calendar dstCal) {

        int hour = dstCal.get(Calendar.HOUR_OF_DAY);
        String timeStr = HOUR_MIN.format(dstCal.getTime());

        String dateStr;
        if (hour >= 0 && hour < 6) {
            dateStr = "凌晨";
        } else if (hour >= 6 && hour < 12) {
            dateStr = "上午";
        } else if (hour >= 12 && hour < 18) {
            dateStr = "下午";
        } else if (hour >= 18 && hour < 24) {
            dateStr = "晚上";
        } else {
            dateStr = "晚上";
        }

        return dateStr + timeStr;
    }
}
