package com.aihoo.util;

import java.util.regex.Pattern;

/**
 * Created by xieyc on 2020/7/30.
 */
public class ReturnValues {

    public static String toString(Object value) {
        String valueStr = null != value & !"".equals(value) ? value.toString() : "";
        return valueStr;
    }

    public static String headImgUrl(Object value){
        String url = "?x-oss-process=image/auto-orient,1/interlace,1/resize,p_50/quality,q_60/sharpen,90";
        String valueStr = null != value & !"".equals(value) ? value.toString() : "";
        String[] split = value.toString().split("[.]");
        if (null != valueStr && !"gif".equals(split[split.length - 1]) && !"MP4".equals(split[split.length - 1]) && !"mp4".equals(split[split.length - 1])) {
            valueStr = valueStr + url;
        }
        return valueStr;
    }

    public static Boolean isNumeric(String longitude,String latitude){
        //经度校验：
        String lonMatch = "[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})";
        //经度校验：
        String latMatch = "[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})";

        Boolean longitudeMatch = Pattern.matches(lonMatch, longitude);
        Boolean latitudeMatch = Pattern.matches(latMatch, latitude);
        if(longitudeMatch&&latitudeMatch){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
    }

}
