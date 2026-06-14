package com.aihoo.api.doctor.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSON;
import com.aihoo.domain.payment.service.ApiService;
import com.aihoo.domain.payment.service.JudgeService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.Map;

/**
 * @program: aihoo-root
 * @description: 回调接口
 * @author: Mr.Li
 * @create: 2020-11-10 17:47
 **/
@RestController
@RequestMapping("/api/v1/api")
public class ApiController {
    Log log = LogFactory.get();
    @Autowired
    private ApiService apiService;
    @Autowired
    private JudgeService judgeService;

    /**
     * 处方状态回调地址
     */
    @RequestMapping(value = "/getPrescriptionLogistics", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResult getPrescriptionLogistics(@RequestBody Map<String, Object> map) throws Exception {
        log.info("\n物流回调\n{}\n",JSON.toJSON(map));
        if (null == map.get("prescripNo") || "".equals(map.get("prescripNo"))) {
            return JsonResult.error(10, "请输入订单编号");
        }
        if (null == map.get("prescripStatus") || "".equals(map.get("prescripStatus"))) {
            return JsonResult.error(10, "请输入订单状态");
        }
        if (null == map.get("dealDate") || "".equals(map.get("dealDate"))) {
            return JsonResult.error(10, "请输入日期");
        }
        String prescripNo = map.get("prescripNo").toString();
        String prescripStatus = map.get("prescripStatus").toString();
        String dealDate = map.get("dealDate").toString();
        log.info(prescripNo);
        log.info(prescripStatus);
        log.info(dealDate);
        boolean status = apiService.getPrescriptionLogistics(prescripNo, prescripStatus, dealDate);
        if (status) {
            log.info("回调处方状态成功");
            return JsonResult.LogisticsOk(0, "回调处方状态成功", status);
        } else {
            log.error("回调处方状态失败");
            return JsonResult.LogisticsError(99, "回调处方状态失败", status);
        }

    }

    /**
     * 推送签名回调地址
     *
     * @param bizSn     业务流水号
     * @param cert      证书
     * @param signAlg   签名算法
     * @param signValue 签名值
     * @param id        用户id
     * @return
     */
    @RequestMapping(value = "/sign", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JsonResult getPushSign(String bizSn, String cert, String signAlg, String signValue, String id) throws Exception {
        log.info(bizSn);
        log.info(URLDecoder.decode(cert, "utf-8"));
        log.info(signAlg);
        log.info(signValue);
        log.info(id);
        boolean status = apiService.getPushSign(id, bizSn, URLDecoder.decode(cert, "utf-8"));
        if (status) {
            log.info("回调认证成功");
        } else {
            log.error("回调认证失败");
        }
        return JsonResult.ok();
    }

    /**
     * 审方回调
     * Certificate=验证信息&userID=医生ID&code=审核结果代码& prescrJson=审核信息
     *
     * @return JsonResult
     */
    @PostMapping(value = "/getResult")
    @ResponseBody
    public JsonResult getResult(String Certificate, String userID, String Code,@RequestParam String prescrJson) {
        try {
            log.info(JSON.toJSONString(Certificate));
            log.info(JSON.toJSONString(userID));
            log.info(JSON.toJSONString(Code));
            log.info(JSON.toJSONString(prescrJson));
            String unescapeJavaScript = StringEscapeUtils.unescapeJson(prescrJson);
            log.info(unescapeJavaScript);
            boolean status = apiService.judgeResult(Certificate, userID, Code, JSON.parseObject(unescapeJavaScript));
            if (status) {
                log.info("回调成功");
                return JsonResult.ok("回调成功");
            } else {
                log.error("回调失败");
                return JsonResult.error("回调失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("回调失败");
        }
    }

    /**
     * 手动审方
     */
    @PostMapping(value = "/judge")
    @ResponseBody
    public JsonResult judge(@RequestBody Map<String, String> map) {
        try {
            if(StrUtil.isBlank(map.get("id"))){
                return JsonResult.error("参数错误");
            }
            judgeService.judge(map.get("id"));
            return JsonResult.ok("调用成功");
        } catch (Exception e) {
            return JsonResult.error("调用失败");
        }
    }

}
