package com.aihoo.domain.sys.service;

import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONObject;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface MKeyService {
    /**
     * 获取二维码
     *
     * @return
     */
    JSONObject qrCode(HttpServletRequest request) throws UnsupportedEncodingException;

    void qrcodeLogin(String bizSn, String action, String cert, String signAlg, String signValue, String id);

    JsonResult qrcodeStatus(String bizSn, HttpServletRequest request);

}
