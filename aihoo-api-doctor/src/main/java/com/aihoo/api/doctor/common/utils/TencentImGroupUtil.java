package com.aihoo.api.doctor.common.utils;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/2 10:43
 */

import com.aihoo.properties.TencentProperties;
import com.aihoo.util.ImUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.aihoo.domain.im.dto.ImSendGroupMsgRequest;
import com.aihoo.api.doctor.app.controller.vo.im.*;
import com.aihoo.api.doctor.common.constant.ImRequestUrlConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 腾讯 IM 群组管理工具类
 * 基于 REST API V4 版本
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class TencentImGroupUtil {

    private final TencentProperties tencentProperties;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();


    private String generateUserSig() {
        return ImUtils.genUserSig(tencentProperties.getAdminidentifier(), null,
                tencentProperties.getSdkappid(), tencentProperties.getPrivstr());
    }

    /**
     * 通用请求执行器
     */
    private <T extends ImRespVo> T executeRequest(String command, Object reqParams, Class<T> responseClass) {
        T result;
        try {
            String url = ImRequestUrlConstant.GROUP_URL + command +
                    "?sdkappid=" + tencentProperties.getSdkappid() +
                    "&identifier=" + tencentProperties.getAdminidentifier() +
                    "&usersig=" + generateUserSig() +
                    "&random=" + (int) (Math.random() * 100000000) +
                    "&contenttype=json";

            String jsonBody = JSON.toJSONString(reqParams);
            RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonBody);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody;
                if (response.body() != null) {
                    responseBody = response.body().string();
                } else {
                    responseBody = "{}";
                }

                if (!response.isSuccessful()) {
                    result = responseClass.getDeclaredConstructor().newInstance();
                    fillErrorResponse(result, response.code(), "网络异常:" + response.message() + "。 信息: " + responseBody);
                    return result;
                }

                try {
                    result = JSON.parseObject(responseBody, responseClass);
                    if (result == null) {
                        result = responseClass.getDeclaredConstructor().newInstance();
                        fillErrorResponse(result, -2, "解析json异常或者请求结果对象为空");
                    }
                } catch (JSONException e) {
                    result = responseClass.getDeclaredConstructor().newInstance();
                    fillErrorResponse(result, -3, "json转换异常 " + e.getMessage());
                }
                return result;
            }
        } catch (IOException e) {
            try {
                result = responseClass.getDeclaredConstructor().newInstance();
                fillErrorResponse(result, -4, "http网络异常: " + e.getMessage());
                return result;
            } catch (Exception ex) {
                throw new RuntimeException("实例化对象异常:", ex);
            }
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("反序列化对象异常" + responseClass.getName(), e);
        }
    }

    /**
     * 辅助方法：填充错误信息到响应对象
     */
    private void fillErrorResponse(ImRespVo response, int errorCode, String errorInfo) {
        response.setActionStatus("FAIL");
        response.setErrorCode(errorCode);
        response.setErrorInfo(errorInfo);
    }

    /**
     * 发送群消息
     */
    public ImSendGroupMsgRespVo sendGroupMessage(ImSendGroupMsgRequest req) {
        if (req.getRandom() == null) {
            req.setRandom((long) (Math.random() * 100000000));
        }
        return executeRequest(ImRequestUrlConstant.SEND_GROUP_MSG, req, ImSendGroupMsgRespVo.class);
    }

}
