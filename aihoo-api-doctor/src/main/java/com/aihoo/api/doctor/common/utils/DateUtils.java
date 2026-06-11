package com.aihoo.api.doctor.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time pattern. Minutes should be mm not MM (MM is month).
 */
@Slf4j
public final class DateUtils {


    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIME_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String TIME_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_YEAR = "yyyy";
    public static final String MMDDYYYY_HHMM = "yyyy-MM-dd HH:mm";
    public static final String RETURN_FORMAT = "yyyy年MM月dd日";

    private DateUtils() {
    }

    public static DateFormat getDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 强制使用东八区
        return sdf;
    }

    /**
     * 格式化日期与时间
     */
    public static String formatDatetime(long timestamp) {
        return getDateFormat(DATETIME_FORMAT).format(new Date(timestamp));
    }

    /**
     * 格式化日期
     */
    public static String formatDate(long timestamp) {
        return getDateFormat(DATE_FORMAT).format(new Date(timestamp));
    }

    public static String formatDate(long timestamp, String pattern) {
        return getDateFormat(pattern).format(new Date(timestamp));
    }

    public static String formatString(String dateStr, String formatString) {
        try {
            Date date = new SimpleDateFormat(formatString).parse(dateStr);
            return new SimpleDateFormat(formatString).format(date);
        } catch (Exception e) {
            log.error("字符串转换指定格式日期失败，" + e.getMessage());
        }
        return "";
    }

    /**
     * 格式化时间
     */
    public static String formatTime(long timestamp) {
        return getDateFormat(TIME_FORMAT).format(new Date(timestamp));
    }

    /**
     * 获取当前日期与时间
     */
    public static String getCurrentDatetime() {
        return getDateFormat(DATETIME_FORMAT).format(new Date());
    }

    /**
     * 获取当前日期与时间
     */
    public static String gettDate2time(Date date) {
        return getDateFormat(DATETIME_FORMAT).format(date);
    }


    /**
     * 获取 +30分钟 当前日期与时间 格式 yyyyMMddHHmmss
     *
     * @return
     */
    public static String getAddTimeCurrentDatetime() {
        return getDateFormat(TIME_FORMAT_YYYYMMDDHHMMSS).format(addMinute(new Date(), 30));
    }

    /**
     * 获取当前日期
     */
    public static String getCurrentDate() {
        return getDateFormat(DATE_FORMAT).format(new Date());
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        return getDateFormat(TIME_FORMAT).format(new Date());
    }

    /**
     * 解析日期与时间
     */
    public static Date parseDatetime(String str) {
        Date date = null;
        try {
            date = getDateFormat(DATETIME_FORMAT).parse(str);
        } catch (ParseException e) {
            log.warn("解析日期字符串出错！格式：yyyy-MM-dd HH:mm:ss", e.getMessage());
        }
        return date;
    }

    /**
     * 解析日期
     */
    public static Date parseDate(String str) {
        Date date = null;
        try {
            date = getDateFormat(DATE_FORMAT).parse(str);
        } catch (ParseException e) {
            log.warn("解析日期字符串出错！格式：yyyy-MM-dd", e.getMessage());
        }
        return date;
    }

    /**
     * 解析日期
     */
    public static Date parseDate(String str, String pattern) {
        Date date = null;
        try {
            date = getDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            log.warn("解析日期字符串出错！格式：yyyy-MM-dd", e.getMessage());
        }
        return date;
    }

    /**
     * 解析时间
     */
    public static Date parseTime(String str) {
        Date date = null;
        try {
            date = getDateFormat(TIME_FORMAT).parse(str);
        } catch (ParseException e) {
            log.error("解析日期字符串出错！格式：HH:mm:ss", e);
        }
        return date;
    }


    /**
     * 获取日期前几天的日期
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    public static Date addMonthDate(Date d, int month) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.MONTH, month);
        return now.getTime();
    }

    /**
     * 获取日期后几天的日期
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static Date getNextDay(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(day));
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
        return now.getTime();
    }


    /**
     * 获取前几月的日期
     *
     * @param d
     * @param m
     * @return
     */
    public static Date getMonthBefore(Date d, int m) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.MONTH, m);
        return now.getTime();
    }


    /**
     * 在指定的时间上加多少分钟
     *
     * @param data
     * @param minute
     * @return
     */
    public static Date addMinute(Date data, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * 当前时间加 @param sec 秒
     *
     * @param sec
     * @return
     */
    public static Date addSecond(int sec) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, sec);
        return calendar.getTime();
    }

    /**
     * 在指定的时间上加多少天
     *
     * @param data
     * @param day
     * @return
     */
    public static Date addDay(Date data, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }


    public static Date getNowDateTime() {
        return new Date();
    }


    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取day 来自month
     *
     * @param date
     * @return
     */
    public static Integer getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 得到时间的星期
     *
     * @param date
     * @return
     */
    public static Integer getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static Integer getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /*
    获取指定时间的下一个月的前一天
     */
    public static Date getLastDay(Date sourceDate, int addMonth, int addDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sourceDate);
        Integer beforeDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.MONTH, addMonth);
        cal.add(Calendar.DATE, addDate);
        Integer year = cal.get(Calendar.YEAR);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        Integer month = cal.get(Calendar.MONTH) + 1;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            //闰年
            if (month == 2 && day == 27 && (beforeDay == 31 || beforeDay == 30)) {
                cal.add(Calendar.DATE, 1);
            }
        } else {
            if (month == 2 && day == 27 && (beforeDay == 31 || beforeDay == 30 || beforeDay == 29)) {
                cal.add(Calendar.DATE, 1);
            }
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (beforeDay == 31) {
                cal.add(Calendar.DATE, 1);
            }
        }
        return cal.getTime();
    }

    /**
     * 获取指定时间月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //calendar.add(Calendar.MONTH, -1);
        //设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定时间月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取指定时间前月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayBeforeMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    //获取周第一天
    public static Date getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }


    public static Date getDayOfWeek(Date date, int a) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_WEEK, a - 1);
        return cal.getTime();
    }

    //获取周最后一天
    public static Date getLastDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 0:0:0 的时间
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Boolean checkDayInToday(Date date) {
        Date end = getDayEnd(new Date());
        Date start = getDayBegin(new Date());
        if (date.before(end) && date.after(start)) {
            return true;
        }
        return false;
    }

    /**
     * 获取指定时间的那天 *:0:0 的时间
     *
     * @param date
     * @return
     */
    public static Date getHourBegin(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }


    public static Date getHourEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /*
    将一个时间的
     */
    public static Date setHour(Date date, Integer hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    public static Date setMinute(Date date, Integer minu) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, minu);
        return c.getTime();
    }

    public static int daysOfTwo(Date fDate, Date oDate) {

        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(fDate);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(oDate);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }

    /**
     * 获取昨天00：00至今天00：00的时间区间
     *
     * @return [0] 昨天 [1] 今天
     */
    public static Date[] getTodayAndYesterdayTimeRange() {
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(current);
        try {
            current = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        current = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date[] dates = new Date[]{calendar.getTime(), current};
        return dates;
    }

    public static Date[] getWeekTime() {

        Date monday = null;
        Date Sunday = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date();
        String d = format.format(current);
        try {
            current = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DAY_OF_MONTH, -1); //解决周日会出现 并到下一周的情况
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR, 0);
        monday = calendar.getTime();
        calendar.add(Calendar.DATE, 6);
        Sunday = calendar.getTime();
        Date[] dates = new Date[]{monday, Sunday};
        return dates;
    }

    /**
     * 获取这个星期一00：00至今天天00：00的时间区间
     *
     * @return [0] 昨天 [1] 今天
     */
    public static Date[] getWeekTimeRange() {
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(current);
        try {
            current = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        current = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date[] dates = new Date[]{calendar.getTime(), current};
        return dates;
    }


    public static Date getDate(int day) {
        Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DATE, day);
        current = calendar.getTime();
        return current;
    }

    public static Date[] getToday() {
        Date date = getTodayAndYesterdayTimeRange()[1];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date date1 = calendar.getTime();
        return new Date[]{date, date1};
    }

    public static Date getTodayFromOldDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH);
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(date);
        newCal.set(Calendar.YEAR, year);
        newCal.set(Calendar.MONTH, month);
        newCal.set(Calendar.DAY_OF_MONTH, day);
        return newCal.getTime();
    }

    public static LocalDateTime str2LocalDateTime(String dateStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        return LocalDateTime.parse(dateStr, df);
    }

    public static void main(String args[]) {
        String dateTimeStr = "2018-07-28 00:00:00";
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, df);
        System.out.println(dateTime);
       /* Date b = DateUtil.getFirstDayOfMonth(new Date());
       b = DateUtil.getDayBegin(b);
        System.out.print(formatDate(b.getTime() ,DateUtil.DATETIME_FORMAT));*/
    }

}
