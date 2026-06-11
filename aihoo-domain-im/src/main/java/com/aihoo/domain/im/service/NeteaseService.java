package com.aihoo.domain.im.service;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public interface NeteaseService {
    /**
     * 创建im用户
     *
     * @param map
     * @return
     */
    JSONObject create(Map<String, String> map);

}
