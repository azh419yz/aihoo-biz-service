package com.aihoo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {
    public static Date getDateByString(String time) {
        if (time == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDayTime(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df.format(date);
    }

    public static String hourFormat(int h) {
        return h < 10 ? "0" + h : String.valueOf(h);
    }
}