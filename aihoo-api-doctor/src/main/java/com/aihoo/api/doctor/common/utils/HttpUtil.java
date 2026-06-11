package com.aihoo.api.doctor.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 请求工具类
 */
@Slf4j
@Component
public class HttpUtil {

    private static RestTemplate restTemplate;
    private static ObjectMapper objectMapper;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        HttpUtil.restTemplate = restTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        HttpUtil.objectMapper = objectMapper;
    }

    /**
     * 发送GET请求并将结果转换为指定类型
     *
     * @param url          请求URL
     * @param responseType 返回类型Class
     * @param <T>          返回类型泛型
     * @return 转换后的对象
     */
    public static <T> T getForObject(String url, Class<T> responseType) {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        
        // 先获取字符串，避免Content-Type问题
        String responseString = restTemplate.getForObject(url, String.class);
        
        if (responseString == null) {
            return null;
        }

        // 如果需要的也是String，直接返回
        if (responseType == String.class) {
            return (T) responseString;
        }

        try {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            return objectMapper.readValue(responseString, responseType);
        } catch (Exception e) {
            log.error("HttpUtil deserialization failed for url: {}", url, e);
            throw new RuntimeException("HTTP请求结果解析失败", e);
        }
    }

    /**
     * 发送POST请求并将结果转换为指定类型
     *
     * @param url          请求URL
     * @param request      请求参数对象
     * @param responseType 返回类型Class
     * @param <T>          返回类型泛型
     * @return 转换后的对象
     */
    public static <T> T post(String url, Object request, Class<T> responseType) {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        try {
            // 将请求对象转换为JSON字符串
            String jsonBody = null;
            if (request != null) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                }
                jsonBody = objectMapper.writeValueAsString(request);
            }

            // 设置 Content-Type 为 application/json
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(jsonBody, headers);

            String responseString = restTemplate.postForObject(url, entity, String.class);

            if (responseString == null) {
                return null;
            }

            if (responseType == String.class) {
                return (T) responseString;
            }

            return objectMapper.readValue(responseString, responseType);
        } catch (Exception e) {
            log.error("HttpUtil post failed for url: {}", url, e);
            throw new RuntimeException("HTTP请求结果解析失败", e);
        }
    }
}
