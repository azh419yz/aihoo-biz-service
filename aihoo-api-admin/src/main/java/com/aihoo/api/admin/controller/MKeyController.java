package com.aihoo.api.admin.controller;

import com.aihoo.constant.LoginErrorCodeEnum;
import com.aihoo.domain.sys.service.MKeyService;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 云医签
 *
 * @author Lenovo
 */
@RestController
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping("/api/v1/mkey")
public class MKeyController {
    @Resource
    private MKeyService mKeyService;

    /**
     * 获取二维码
     *
     * @return
     */
    @RequestMapping(value = "/qrcode", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResult qrcode(HttpServletRequest request) {
        try {
            JSONObject qrcode = mKeyService.qrCode(request);
            if (null == qrcode) {
                return JsonResult.error();
            }
            return JsonResult.ok().put("data", qrcode);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 接收扫码确认，以及接收数据
     *
     * @param bizSn     业务流水号
     * @param action    confirm or login
     * @param cert      证书
     * @param signAlg   签名算法
     * @param signValue 签名值
     */
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResult qrcodeLogin(String bizSn, String action, String cert, String signAlg, String signValue, String id) {
        
        try {
            mKeyService.qrcodeLogin(bizSn, action, cert, signAlg, signValue, id);
            return JsonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 接收扫码确认，以及接收数据
     *
     * @param map 业务流水号
     */
    @RequestMapping(value = "/status", method = {RequestMethod.GET, RequestMethod.POST})
    public JsonResult qrcodeStatus(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            return mKeyService.qrcodeStatus(map.get("bizSn").toString(), request);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(LoginErrorCodeEnum.QRCODE_ERROR, "系统繁忙");
        }
    }
}
