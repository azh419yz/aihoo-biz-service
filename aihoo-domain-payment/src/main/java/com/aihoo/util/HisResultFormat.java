package com.aihoo.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import java.util.*;

/**
 * @Classname HisResultFormat
 * @Description hf
 * @Date 2021/2/27 10:56
 * @Created by ad
 */
public class HisResultFormat {

    /**
     * 格式化his 出参
     *
     * @param data 入参
     * @return {}
     */
    public static List<Map<String, String>> getMaps(Object data) {
        if (null == data) {
            return new ArrayList<>();
        }
        if (data instanceof List) {
            return JSON.parseObject(JSON.toJSONString(data), new TypeReference<List<Map<String, String>>>() {
            });
        } else {
            JSONObject object = JSON.parseObject(JSON.toJSONString(data));
            Map<String, String> map = JsonToMap(object);
            List<Map<String, String>> list = new ArrayList<>();
            list.add(map);
            return list;
        }

    }

    //json对象转为map
    public static Map<String, String> JsonToMap(JSONObject j) {
        Map<String, String> map = new HashMap<>();
        Iterator<String> iterator = j.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = (String) j.get(key);
            map.put(key, value);
        }
        return map;
    }
}
