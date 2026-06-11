package com.aihoo.api.doctor.common.utils;



import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class MapBaiduUtils {

    private static final String BAIDU_APP_KEY = "tDVAfuCUqVcL8Xf44vpFLLdXZBpIK2hT";

    /**
     * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
     */
    public static Map<String, String> getLatitude(String address) {
        try {
            // 将地址转换成utf-8的16进制
            address = URLEncoder.encode(address, "UTF-8");
            // 如果有代理，要设置代理，没代理可注释
            // System.setProperty("http.proxyHost","192.168.172.23");
            // System.setProperty("http.proxyPort","3209");

            URL resjson = new URL("http://api.map.baidu.com/geocoder?address="
                    + address + "&output=json&key=" + BAIDU_APP_KEY);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    resjson.openStream()));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            System.out.println("return json:" + str);
            if(str!=null&&!str.equals("")){
                Map<String, String> map = null;
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);
                    map = new HashMap<String, String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /***
     * 获取地理位置
     *
     * @param lat
     * 纬度
     * @param lng
     * 经度
     * @return JSON 地理位置
     * @throws IOException
     */
    public static JSONObject getLocation(String lat, String lng) throws Exception {
        String location = lat+","+lng;
        URL url = new URL("http://api.map.baidu.com/geocoder?ak=" + BAIDU_APP_KEY
                + "&callback=renderReverse&location="+location+"&output=json");
        URLConnection connection = url.openConnection();
        /*
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        out.flush();
        out.close();
        // 一旦发送成功，用以下方法就可以得到服务器的回应：
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(l_urlStream, "UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotEmpty(str)) {
            int addressStart = str.indexOf("formatted_address\":");
            int addressEnd = str.indexOf("\",\"business");
            int provinceStart = str.indexOf("province\":");
            int provinceEnd = str.indexOf("\",\"street");
            int cityStart = str.indexOf("city\":");
            int cityEnd = str.indexOf("\",\"direction");
            int districtStart = str.indexOf("district\":");
            int districtEnd = str.indexOf("\",\"province");

            if (addressStart > 0 && addressEnd > 0) {
                String address = str.substring(addressStart + 20, addressEnd);
                String province = str.substring(provinceStart + 11, provinceEnd);
                String city = str.substring(cityStart + 7, cityEnd);
                String district = str.substring(districtStart +11, districtEnd);
                jsonObject.put("address", address);
                jsonObject.put("province", province);
                jsonObject.put("city", city);
                jsonObject.put("district", district);
                return jsonObject;
            }
        }
        return jsonObject;
    }


    public static void main(String args[]) throws Exception{

//        Map<String, String> map = MapBaidu.getLatitude("安徽池州市贵池区金碧秋浦34号");
//        if (null != map) {
//            System.out.println(map.get("lng"));
//            System.out.println(map.get("lat"));
//        }
//        JSONObject jsonObject = MapBaidu.getLocation(30.667136+"",117.473191+"");
//        System.out.println(jsonObject);
    }
}
