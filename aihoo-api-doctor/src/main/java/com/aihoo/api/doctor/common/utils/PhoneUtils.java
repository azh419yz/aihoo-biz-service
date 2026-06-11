package com.aihoo.api.doctor.common.utils;

import cn.hutool.http.HttpRequest;
import com.aihoo.util.PropertyReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 极光获取手机号
 */
@Slf4j
public class PhoneUtils {

    static final Properties CONFIG = PropertyReader.getProperties("/config/phone.properties");

    private static String appKey = CONFIG.getProperty("jiguang.appKey");

    private static String Secret = CONFIG.getProperty("jiguang.Secret");

    private static String prikey = CONFIG.getProperty("jiguang.prikey");

    public static JSONObject loginTokenVerify(String loginToken) {
        String host = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
        Map<String, Object> map = new HashMap<>();
        map.put("loginToken", loginToken);
        map.put("exID", null);

        String result = HttpRequest.post(host)
                .header("Authorization", "Basic " + Base64.getUrlEncoder()
                        .encodeToString((appKey + ":" + Secret).getBytes()))
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(JSON.toJSONString(map))
                .execute().body();

        log.info("result:[{}]", result);

        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject;
    }

    public static String decrypt(String encryptPhone) throws Exception {

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] b = Base64.getDecoder().decode(encryptPhone);

        return new String(cipher.doFinal(b));
    }
}
