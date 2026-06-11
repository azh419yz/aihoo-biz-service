package com.aihoo.api.doctor.common.utils;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: aihoo-root
 * @description: 私钥加密
 * @author: Mr.Li
 * @create: 2020-10-20 13:22
 **/
@Component
public class HospitalPrescriptionUtil {
    private Log log = LogFactory.get();

//    private String REQ_URL = "https://www.yiyaogo.com/apitest/prescriptionService/addSingle";

//    private String REQS_URL = "https://www.yiyaogo.com/apitest/logisticsService/fetchLogisticsProcess";

//    private String ACCESS_APPID = PropertiesConfig.Hospital_Prescription_APPID;

    private long TIMESTAMP = System.currentTimeMillis();

//    private String APP_SECRET = PropertiesConfig.Hospital_Prescription_APPSECRET;

    /**
     * @param prescripNo         处方单号
     * @param prescribeDate      处方日期 Number
     * @param pharmacyFlag       配药药房（0-云药房；1-医院药房）
     * @param hospitalCode       医疗机构组织代码（22位）
     * @param hospitalName       医院名称
     * @param department         科室名称 选填
     * @param doctorName         医生名称 选填
     * @param name               患者姓名
     * @param sex                性别(0:女;1:男) Number
     * @param age                年龄 Number 选填
     * @param mobile             手机 选填
     * @param idCard             身份证号 选填
     * @param socialSecurityCard 社保卡号／医保卡／就诊卡号 选填
     * @param address            地址（卡内登记地址） 选填
     * @param feeType            费别(0-医保；1-自费) Number
     * @param totalAmount        总金额（退费时为负数）
     * @param diagnoseResult     诊断结果
     * @param receiver           收货人名称（送药到家）
     * @param receiverMobile     收货人联系方式（送药到家）
     * @param provinceName       省份（送药到家）
     * @param cityName           市（送药到家）
     * @param districtName       区县名称（送药到家）
     * @param shippingAddress    配送地址（送药到家）
     * @param remark             备注
     * @param details            处方明细 : medName药品名称,medCode药品代码（医保编码或HIS编码）,spec规格,drugForm剂形,directions用法用量,unitName单位名称,(amount数量（退费时为负数）,unitPrice单价)Number
     */
    //details 中 amount和unitPrice是Number
//    @Async(value = "asyncExecutor")
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

    //    @Async(value = "asyncExecutorAdmin")
    public JSONObject prescriptionLogistics(String REQS_URL, String ACCESS_APPID, String APP_SECRET,
                                            String prescripNo, String prescribeDate, String hospitalName) throws Exception {
        log.info("查询处方物流开始-- 订单号" + prescripNo);
        TIMESTAMP = System.currentTimeMillis();
        log.info("\n{}\n{}\n{}\n{}\n{}\n{}", REQS_URL, ACCESS_APPID, APP_SECRET, prescripNo, hospitalName, TIMESTAMP);
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

//    @Test
//    public void reqTestsa() throws Exception {
//        String data = "{\"prescripNo\": \"P20201111132349005\",\"prescribeDate\": \"\",\"hospitalName\": \"上海合偶平方互联网医院\"}";
//        String siganature = AppSiganatureUtils.createSiganature(data, ACCESS_APPID, APP_SECRET, TIMESTAMP);
//        System.out.println("签名：" + siganature);
//        String s = Request.Post(REQS_URL)
//                .setHeader("ACCESS_APPID", ACCESS_APPID)
//                .setHeader("ACCESS_TIMESTAMP", String.valueOf(TIMESTAMP))
//                .setHeader("ACCESS_SIGANATURE", siganature)
//                .bodyString(data, ContentType.APPLICATION_JSON)
//                .execute().returnContent().asString();
//        JSONObject jsonObject = JSONObject.parseObject(s);
//        System.out.println(s);
//        System.out.println(jsonObject.getString("result"));
//    }
//
//    //
//    @Test
//    public void reqTestssss() throws Exception {
//        String data = "{\"prescripNo\": \"P20201111132349003\",\"prescribeDate\": 178202020, \"pharmacyFlag\": \"0\",\"hospitalCode\": \"001\",\"hospitalName\": \"上海合偶平方互联网医院\",\"department\": \"儿童科\",\"doctorName\": \"1111\",\"name\": \"msh\",\"sex\": 0,\"age\": 20,\"mobile\": \"15534663807\",\"idCard\": \"140431199608070035\",\"socialSecurityCard\": \"\",\"address\": \"\",\"feeType\": 0,\"totalAmount\": 300,\"diagnoseResult\": \"11111\",\"receiver\": \"1111\",\"receiverMobile\": \"15534663807\",\"provinceName\": \"上海\",\"cityName\": \"上海市\",\"districtName\": \"浦东新区\",\"shippingAddress\": \"福山路102弄\",\"remark\": \"\",\"details\": [{\"medName\": \"111\",\"medCode\": \"111\",\"spec\": \"11\",\"drugForm\": \"1\",\"directions\": \"1\",\"amount\": 3,\"unitPrice\": 100}]}";
//        String siganature = AppSiganatureUtils.createSiganature(data, ACCESS_APPID, APP_SECRET, TIMESTAMP);
//        System.out.println("签名：" + siganature);
//        String s = Request.Post(REQ_URL)
//                .setHeader("ACCESS_APPID", ACCESS_APPID)
//                .setHeader("ACCESS_TIMESTAMP", String.valueOf(TIMESTAMP))
//                .setHeader("ACCESS_SIGANATURE", siganature)
//                .bodyString(data, ContentType.APPLICATION_JSON)
//                .execute().returnContent().asString();
//        System.out.println(s);
//    }

}
