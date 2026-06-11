package com.aihoo.api.doctor.common.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xieyc on 2020/8/3.
 */
public class TimeUtil {
    public static Date getDateByString(String time) {
        Date date = null;
        if (time == null) {
            return date;
        }
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getShortTime(String time) {
        String shortString = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if (date == null) {
            return shortString;
        }
        long delTime = (now - date.getTime()) / 1000;


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        Date yesterdayTime = calendar.getTime();

        if ((delTime <= (2 * 24 * 60 * 60)) && (date.getTime() > yesterdayTime.getTime()) && (delTime > (24 * 60 * 60))) {
            shortString = "昨天" + time.substring(11, 16);
        } else if (delTime <= 24 * 60 * 60 && delTime > 60 * 60) {
            shortString = (int) (delTime / (60 * 60)) + "小时前";
        } else if (delTime <= 60 * 60 && delTime > 60) {
            shortString = (int) (delTime / (60)) + "分前";
        } else if (delTime <= 60 && delTime >= 0) {
            shortString = delTime + "秒前";
        } else {
            shortString = time.substring(5, 16);
        }
        return shortString;
    }


    public static String getDayTime(Date date, String pattern) {

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        return df.format(date);

    }

    /**
     * 仅24小时倒计时可用,返回剩余秒数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    public static String getBetweenTime(Date start, Date end, Integer cutDown) {
        long between = cutDown * 1000 - start.getTime() + end.getTime();
        long day = between / (24 * 60 * 60 * 1000);
        if (day != 0) return "0";
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (hour < 0 || min < 0 || s < 0) {
            return "0";
        }
        return hour * 60 * 60 + min * 60 + "";
    }

    public static void main(String[] args) {
/*        String time = getShortTime("2020-08-01 23:10:00");
        System.out.println(time);*/

/*        String url="http://uchanlife.oss-us-east-1.aliyuncs.com/message/20200807/202008071505186c1cfca153394cf7bcf517cd83b07b0c.jpg?Expires=4750383941&OSSAccessKeyId=LTAI4G2U9mtHh5XdCuzmhAhQ&Signature=ieOsotRw8jJnZaLP0%2FAUM4IqIOE%3D";
        String sadad = OssFileUtils.uploadFileURL("sadad", url);
        System.out.println(url);*/

/*        JSONObject jsonObject=new JSONObject();
        jsonObject.put("status",false);

        if ((Boolean) jsonObject.get("status")){
            String str="http://uchanlife.oss-us-east-1.aliyuncs.com/headImg/20200811091653fd60e653f85543428b1aa818c04ba9ec.png?Expires=4750708613&OSSAccessKeyId=LTAI4G2U9mtHh5XdCuzmhAhQ&Signature=EarBOwcdhGrJaqHP5keMig9726M%3D";
            String[] split = str.split("[?]");
            System.out.println(split[0]);
        }*/

        Map<String, Object> map = new HashMap<>();
        map.put("childCategoryIds", null);

        List<String> childCategoryIds = (List<String>) map.get("childCategoryIds");
        System.out.println(childCategoryIds);

        String pp = "家具222222222222222222222222222222222222222222222222";
        System.out.println(pp.length());


    }

}
