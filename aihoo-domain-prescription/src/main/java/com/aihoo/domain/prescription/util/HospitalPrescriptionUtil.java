package com.aihoo.domain.prescription.util;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson2.JSONObject;
import com.aihoo.util.AppSiganatureUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @program: aihoo-root
 * @description: 处方流转平台工具（查询处方物流）
 * @author: Mr.Li
 * @create: 2020-10-20 13:22
 * <p>
 * 当前状态：占位实现，仅保留与旧代码一致的方法签名。具体 HTTP 逻辑待 shared-kernel 引入 AppSiganatureUtils/Request 后回填。
 * </p>
 **/
@Component
public class HospitalPrescriptionUtil {
    private Log log = LogFactory.get();

    private long TIMESTAMP = System.currentTimeMillis();

    /**
     * 查询处方物流
     *
     * @param REQS_URL       物流查询接口地址
     * @param ACCESS_APPID   应用ID
     * @param APP_SECRET     应用密钥
     * @param prescripNo     处方单号
     * @param prescribeDate  处方日期
     * @param hospitalName   医院名称
     * @return 物流查询结果
     */
    public JSONObject prescriptionLogistics(String REQS_URL, String ACCESS_APPID, String APP_SECRET,
                                            String prescripNo, String prescribeDate, String hospitalName) throws Exception {
        log.info("查询处方物流开始-- 订单号" + prescripNo);
        TIMESTAMP = System.currentTimeMillis();
        log.info("REQS_URL={} ACCESS_APPID={} APP_SECRET={} prescripNo={} hospitalName={} TIMESTAMP={}", REQS_URL, ACCESS_APPID, APP_SECRET, prescripNo, hospitalName, TIMESTAMP);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prescripNo", prescripNo);
        jsonObject.put("prescribeDate", prescribeDate);
        jsonObject.put("hospitalName", hospitalName);
        String data = jsonObject.toString();
        String siganature = AppSiganatureUtils.createSiganature(data, ACCESS_APPID, APP_SECRET, TIMESTAMP);
        System.out.println("签名：" + siganature);
        String s = Request.Post(REQS_URL)
                .setHeader("ACCESS_APPID", ACCESS_APPID)
                .setHeader("ACCESS_TIMESTAMP", String.valueOf(TIMESTAMP))
                .setHeader("ACCESS_SIGANATURE", siganature)
                .bodyString(data, ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();
        System.out.println(s);
        JSONObject json = JSONObject.parseObject(s);
        log.info("查询处方物流结束-- 订单号" + prescripNo);
        return json;
//        return map;
    }



    public JSONObject savePrescription(String REQ_URL, String ACCESS_APPID, String APP_SECRET,
                                       String prescripNo, Number prescribeDate, String pharmacyFlag, String hospitalCode, String hospitalName, String department, String doctorName, String name, Number age, Number sex, String mobile, String idCard, String socialSecurityCard, String address, Number feeType, Number totalAmount, String diagnoseResult, String receiver, String receiverMobile, String provinceName, String cityName, String districtName, String shippingAddress, String remark, List<JSONObject> details) throws Exception {
        log.info("新增处方开始-- 订单号" + prescripNo);
        TIMESTAMP = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prescripNo", prescripNo);
        jsonObject.put("prescribeDate", prescribeDate);
        jsonObject.put("pharmacyFlag", pharmacyFlag);
        jsonObject.put("hospitalCode", hospitalCode);
        jsonObject.put("hospitalName", hospitalName);
        jsonObject.put("department", department);
        jsonObject.put("doctorName", doctorName);
        jsonObject.put("name", name);
        jsonObject.put("age", age);
        jsonObject.put("sex", sex);
        jsonObject.put("mobile", mobile);
        jsonObject.put("idCard", idCard);
        jsonObject.put("socialSecurityCard", socialSecurityCard);
        jsonObject.put("address", address);
        jsonObject.put("feeType", feeType);
        jsonObject.put("totalAmount", totalAmount);
        jsonObject.put("diagnoseResult", diagnoseResult);
        jsonObject.put("receiver", receiver);
        jsonObject.put("receiverMobile", receiverMobile);
        jsonObject.put("provinceName", provinceName);
        jsonObject.put("cityName", cityName);
        jsonObject.put("districtName", districtName);
        jsonObject.put("remark", remark);
        jsonObject.put("shippingAddress", shippingAddress);
        jsonObject.put("details", details);
        String data = jsonObject.toString();
        String siganature = AppSiganatureUtils.createSiganature(data, ACCESS_APPID, APP_SECRET, TIMESTAMP);
        System.out.println("签名：" + siganature);
        String s = Request.Post(REQ_URL)
                .setHeader("ACCESS_APPID", ACCESS_APPID)
                .setHeader("ACCESS_TIMESTAMP", String.valueOf(TIMESTAMP))
                .setHeader("ACCESS_SIGANATURE", siganature)
                .bodyString(data, ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();
        JSONObject json = JSONObject.parseObject(s);
        log.info(s);
        log.info("新增处方结束-- 订单号" + prescripNo);
        return json;
}
}
