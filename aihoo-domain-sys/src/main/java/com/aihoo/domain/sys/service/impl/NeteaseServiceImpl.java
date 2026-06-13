package com.aihoo.domain.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.properties.NeteaseProperties;
import com.aihoo.util.CheckSumBuilder;
import com.aihoo.domain.sys.service.NeteaseService;
import com.aihoo.exception.BizException;
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
        /**
         * 参数	    类型	    必须	说明
         * accid	String	是	网易云信IM账号，最大长度32字符，必须保证一个
         *                      APP内唯一（只允许字母、数字、半角下划线_、
         *                      @、半角点以及半角-组成，不区分大小写，
         *                      会统一小写处理，请注意以此接口返回结果中的accid为准）。
         * name 	String	否	网易云信IM账号昵称，最大长度64字符。
         * props	String	否	json属性，开发者可选填，最大长度1024字符。该参数已不建议使用。
         * icon	    String	否	网易云信IM账号头像URL，开发者可选填，最大长度1024
         * token	String	否	网易云信IM账号可以指定登录IM token值，最大长度128字符，
         *                      并更新，如果未指定，会自动生成token，并在
         *                      创建成功后返回
         * sign 	String	否	用户签名，最大长度256字符
         * email	String	否	用户email，最大长度64字符
         * birth	String	否	用户生日，最大长度16字符
         * mobile	String	否	用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)
         * gender	int	    否	用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
         * ex   	String	否	用户名片扩展字段，最大长度1024字符，用户可自行扩展，建议封装成JSON字符串
         */
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
