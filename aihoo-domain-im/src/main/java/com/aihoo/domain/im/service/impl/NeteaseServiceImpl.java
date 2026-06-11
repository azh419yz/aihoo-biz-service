package com.aihoo.domain.im.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.exception.BizException;
import com.aihoo.properties.NeteaseProperties;
import com.aihoo.util.CheckSumBuilder;
import com.aihoo.domain.im.service.NeteaseService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易云信封装
 *
 * @author Lenovo
 */
@Component
public class NeteaseServiceImpl implements NeteaseService {
    private Log log = LogFactory.get();
    @Resource
    private NeteaseProperties neteaseProperties;

    /**
     * AppKey	开发者平台分配的appkey
     * Nonce	随机数（最大长度128个字符）
     * CurTime	当前UTC时间戳，从1970年1月1日0点0 分0 秒开始到现在的秒数(String)
     * CheckSum	SHA1(AppSecret + Nonce + CurTime)，三个参数拼接的字符串，进行SHA1哈希计算，转化成16进制字符(String，小写)
     */
    @Override
    public JSONObject create(Map<String,String> map) {
        if(StrUtil.isBlank(map.get("accid"))){
            log.error("请求网易云信创建用户函数accid为空");
            throw new BizException("accid is null");
        }
        /*请求头*/
        String appKey = neteaseProperties.getAppKey();
        String nonce = RandomUtil.randomString(8);
        String curTime = String.valueOf(DateUtil.date().getTime() / 1000L);
        String appSecret = neteaseProperties.getAppSecret();
        /*请求参数*/
        Map<String, Object> paramMap = new HashMap<>(15);
        paramMap.put("accid", map.get("accid"));
        if(StrUtil.isNotBlank(map.get("name"))) {
            paramMap.put("name", map.get("name"));
        }
        if(StrUtil.isNotBlank(map.get("props"))) {
            paramMap.put("props", map.get("props"));
        }
        if(StrUtil.isNotBlank(map.get("icon"))) {
            paramMap.put("icon", map.get("icon"));
        }
        if(StrUtil.isNotBlank(map.get("token"))) {
            paramMap.put("token", map.get("token"));
        }
        if(StrUtil.isNotBlank(map.get("sign"))) {
            paramMap.put("sign", map.get("sign"));
        }
        if(StrUtil.isNotBlank(map.get("email"))) {
            paramMap.put("email", map.get("email"));
        }
        if(StrUtil.isNotBlank(map.get("birth"))) {
            paramMap.put("birth", map.get("birth"));
        }
        if(StrUtil.isNotBlank(map.get("mobile"))) {
            paramMap.put("mobile", map.get("mobile"));
        }
        if(StrUtil.isNotBlank(map.get("gender"))) {
            paramMap.put("gender", map.get("gender"));
        }
        if(StrUtil.isNotBlank(map.get("ex"))) {
            paramMap.put("ex", map.get("ex"));
        }
        String body = HttpUtil.createPost(neteaseProperties.getCreateUrl())
                .header("AppKey", appKey)
                .header("Nonce", nonce)
                .header("CurTime", curTime)
                .header("CheckSum", CheckSumBuilder.getCheckSum(appSecret, nonce, curTime))
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .form(paramMap)
                .execute().body();
        log.info("form:{},body:{}",paramMap,body);
        return JSON.parseObject(body);
    }
}
