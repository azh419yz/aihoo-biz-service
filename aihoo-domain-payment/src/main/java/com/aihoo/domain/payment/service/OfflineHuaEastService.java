package com.aihoo.domain.payment.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 华东医院的 service层
 **/
public interface OfflineHuaEastService {

    /**
     * 华东医院 挂号接口
     */
    Map<String, Object> configBooking(Map<String, Object> map);

    /**
     * 取消预约
     */
    boolean delReservation(Map<String, Object> map);

    /**
     * 回调地址
     */
    String findNotifyUrl(String payInfo);

    /**
     * 发送短信
     */
    String sendMessage(Map<String, Object> map);

    /**
     * 导出
     */
    void orderYueOutExcel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response);

    /**
     * 发送短信
     */
    String resendMessage(Map<String, Object> map);

}