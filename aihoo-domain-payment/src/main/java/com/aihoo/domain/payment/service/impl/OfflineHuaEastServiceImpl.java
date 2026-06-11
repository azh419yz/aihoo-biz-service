package com.aihoo.domain.payment.service.impl;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aihoo.common.HospitalOrderResult;
import com.aihoo.domain.payment.config.HospitalProperties;
import com.aihoo.domain.payment.model.entity.OfflineOder;
import com.aihoo.domain.payment.model.entity.OfflineOderYue;
import com.aihoo.domain.payment.model.entity.OfflineYue;
import com.aihoo.domain.payment.model.mapper.OfflineOderMapper;
import com.aihoo.domain.payment.model.mapper.OfflineOderYueMapper;
import com.aihoo.domain.payment.model.mapper.OfflineYueMapper;
import com.aihoo.domain.payment.model.vo.OfflineConfirmBooking;
import com.aihoo.domain.payment.model.vo.OfflineDistrict;
import com.aihoo.domain.payment.service.OfflineHuaEastService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.util.HospitalUtil;
import com.aihoo.util.SecurityUtils;
import com.aihoo.domain.payment.util.SmsUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.Cipher;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 华东医院 Service 实现
 *
 * <p>原 OfflineHuaEastServiceImpl 同时引用 admin 的 SysUser 和 HospitalProperties，
 * 现已统一到 shared-kernel（com.aihoo.*）。</p>
 *
 * @author lenovo
 */
@Slf4j
@Service
public class OfflineHuaEastServiceImpl implements OfflineHuaEastService {

    @Resource
    private OfflineOderMapper mapper;

    @Resource
    private OfflineOderYueMapper oderYueMapper;

    @Resource
    private OfflineYueMapper yueMapper;

    @Resource
    private HospitalProperties properties;

    /**
     * 挂号接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> configBooking(Map<String, Object> map) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!"2".equals(map.get("groupId")) && !"13".equals(map.get("groupId"))) {
            Map<String, Object> booking = findConfigBooking(map);
            if ("1".equals(booking.get("Result")) || "操作成功".equals(booking.get("ResultInfo"))) {
                OfflineOderYue oderYue = insertOderYue(map);
                oderYue.setYueTime(booking.get("BookingInfo").toString());
                oderYue.setYueId(booking.get("QueueNo").toString());
                oderYue.setFloor(booking.get("Floor").toString());
                oderYue.setBookingId(booking.get("BookingID").toString());
                if ("1".equals(map.get("customer"))) {
                    oderYue.setIsStatus("PAID");
                    oderYue.setIsStatusCode("PAID");
                    if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
                        QueryWrapper<OfflineOder> wrapper = new QueryWrapper<>();
                        wrapper.eq("id", map.get("orderId").toString());
                        OfflineOder offlineOder = this.mapper.selectById(map.get("orderId").toString());
                        offlineOder.setStatus("待定");
                        offlineOder.setHospitalName("华山");
                        offlineOder.setDoctorSpecialty(map.get("codeName").toString());
                        offlineOder.setDoctorName(map.get("staffName").toString());
                        String[] infos = booking.get("BookingInfo").toString().split("-");
                        offlineOder.setVisitTime(map.get("schedulingDate").toString() +" "+ infos[0]);
                        offlineOder.setPrice(map.get("money").toString());
                        this.mapper.update(offlineOder, wrapper);
                    }
                } else {
                    oderYue.setIsStatus("SUCCESS");
                    oderYue.setIsStatusCode("SUCCESS");
                    if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
                        QueryWrapper<OfflineOder> wrapper = new QueryWrapper<>();
                        wrapper.eq("id", map.get("orderId").toString());
                        OfflineOder offlineOder = this.mapper.selectById(map.get("orderId").toString());
                        offlineOder.setStatus("已安排未通知");
                        offlineOder.setHospitalName("华山");
                        offlineOder.setDoctorSpecialty(map.get("codeName").toString());
                        offlineOder.setDoctorName(map.get("staffName").toString());
                        String[] infos = booking.get("BookingInfo").toString().split("-");
                        offlineOder.setVisitTime(map.get("schedulingDate").toString() +" "+ infos[0]);
                        offlineOder.setPrice(map.get("money").toString());
                        this.mapper.update(offlineOder, wrapper);
                    }
                    OfflineDistrict offlineDistrict = this.oderYueMapper.findDistrictName(map.get("districtName").toString());
                    Map<String, Object> hashMap = new HashMap<>();
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(oderYue.getPhone()).append(",")
                            .append(oderYue.getName()).append(",")
                            .append(booking.get("BookingID").toString()).append(",")
                            .append(oderYue.getSchedulingDate()).append(",")
                            .append(oderYue.getDistrictName()).append(",")
                            .append(oderYue.getCodeName()).append(",")
                            .append(oderYue.getStaffName()).append(",")
                            .append(oderYue.getGroupName()).append(",")
                            .append(oderYue.getSchedulingDate()).append(" ").append(booking.get("BookingInfo").toString()).append(",")
                            .append(offlineDistrict.getDistrictAddress()).append(booking.get("Floor").toString()).append(",")
                            .append(booking.get("QueueNo").toString());
                    hashMap.put("msg", "【华山医院】尊敬的{$var}，您的订单{$var}已预约{$var}{$var}的{$var}{$var}{$var}门诊。请于{$var}携带本人身份证原件或户口本原件(限儿童)到{$var}挂号，就诊号{$var}，超过预约时段不保留号源。就诊时请带好所有既往就诊资料。请保留短信以做凭证，转发短信无效。如您不能按约就诊需提前两个工作日撤销，否则将记录失约，半年内失约两次暂停预约资格6个月。一个月内订单只能取消3次。请保持手机畅通，关注预约就诊信息。最终就诊以医院当日信息为准。");
                    hashMap.put("mobile", buffer);
                    System.out.println(SmsUtils.offlineSend(hashMap));
                }
                this.oderYueMapper.insertOderYue(oderYue);
                OfflineOderYue oderYueId = this.oderYueMapper.findOderYueId();
                objectMap.put("status", booking.get("Result").toString());
                objectMap.put("message", booking.get("ResultInfo").toString());
                objectMap.put("bookingId", booking.get("BookingID").toString());
                objectMap.put("id", oderYueId.getId());
                return objectMap;
            }
            OfflineOderYue oderYue = insertOderYue(map);
            oderYue.setIsStatus("FAIL");
            oderYue.setIsStatusCode("FAIL");
            this.oderYueMapper.insertOderYue(oderYue);
            objectMap.put("status", booking.get("Result").toString());
            objectMap.put("message", booking.get("ResultInfo").toString());
            return objectMap;
        } else {
            String method = "Booking.ConfirmBooking";
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("ClassId", map.get("schedulingId"));
            hashMap.put("ReturnFlag", "2");
            hashMap.put("CardType", map.get("customerType"));
            hashMap.put("CardNo", map.get("customerNo"));
            hashMap.put("Name", map.get("customerName"));
            hashMap.put("Mobile", map.get("customerTel"));
            hashMap.put("PoolId", map.get("groupId"));
            OfflineConfirmBooking booking = new OfflineConfirmBooking();
            try {
                JSONObject result = HospitalUtil.SplicingRequest(method, hashMap);
                log.info("华东医院挂号结果:{}", result);
                if ("1".equals(result.get("Status").toString())) {
                    JSONObject data = result.getJSONObject("Parameters");
                    booking = createConfirmBooking(data);
                    OfflineOderYue oderYue = createOderYue(booking, map);
                    this.oderYueMapper.insert(oderYue);
                    OfflineOderYue oderYueId = this.oderYueMapper.findOderYueId();
                    if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
                        QueryWrapper<OfflineOder> wrapper = new QueryWrapper<>();
                        wrapper.eq("id", map.get("orderId").toString());
                        OfflineOder offlineOder = this.mapper.selectById(map.get("orderId").toString());
                        offlineOder.setStatus("已安排未通知");
                        offlineOder.setHospitalName("华东");
                        offlineOder.setDoctorSpecialty(map.get("codeName").toString());
                        offlineOder.setDoctorName(map.get("staffName").toString());
                        String[] split = oderYue.getYueTime().split("-");
                        offlineOder.setVisitTime(map.get("schedulingDate").toString() +" "+ split[0]);
                        offlineOder.setPrice(map.get("money").toString());
                        this.mapper.update(offlineOder, wrapper);
                    }
                    objectMap.put("status", result.get("Status").toString());
                    objectMap.put("message", result.get("Message").toString());
                    objectMap.put("bookingId", booking.getBookingId());
                    objectMap.put("id", oderYueId.getId());
                    return objectMap;
                }
                OfflineOderYue oderYue = insertOderYue(map);
                oderYue.setIsStatus("FAIL");
                oderYue.setIsStatusCode("FAIL");
                this.oderYueMapper.insertOderYue(oderYue);
                objectMap.put("status", result.get("Status").toString());
                objectMap.put("message", result.get("Message").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return objectMap;
        }
    }


    /**
     * 华山医院挂号接口
     */
    private Map<String, Object> findConfigBooking(Map<String, Object> map) {
        Map<String, Object> hashMap = new HashMap<>();
        String url = properties.getAppointmentUrl();
        Document xml = XmlUtil.createXml();
        xml.setXmlStandalone(true);
        Element element = xml.createElement("SERVICES");
        if (map.get("date").equals("1")) {
            element.setAttribute("sname", "HS.BOOKING.ConfirmBooking.BookingTime");
        } else {
            element.setAttribute("sname", "HS.BOOKING.ConfirmBooking");
        }
        xml.appendChild(element);
        Element params = xml.createElement("PARAMS");
        element.appendChild(params);
        Element param = xml.createElement("PARAM");
        param.setAttribute("pname", "IdentifyID");
        param.setAttribute("pval", properties.getDistinguishKey());
        Element param1 = xml.createElement("PARAM");
        param1.setAttribute("pname", "CustomerNo");
        param1.setAttribute("pval", map.get("customerNo").toString());
        if (!"1".equals(map.get("customerType").toString()) && !"2".equals(map.get("customerType").toString())) {
            map.put("customerType","3");
        }
        Element param2 = xml.createElement("PARAM");
        param2.setAttribute("pname", "CustomerNoType");
        param2.setAttribute("pval", map.get("customerType").toString());
        Element param3 = xml.createElement("PARAM");
        param3.setAttribute("pname", "CustomerName");
        param3.setAttribute("pval", map.get("customerName").toString());
        Element param4 = xml.createElement("PARAM");
        param4.setAttribute("pname", "CustomerTel");
        param4.setAttribute("pval", map.get("customerTel").toString());
        Element param5 = xml.createElement("PARAM");
        param5.setAttribute("pname", "SchedulingID");
        param5.setAttribute("pval", map.get("schedulingId").toString());
        if ("1".equals(map.get("customer").toString())) {
            Element param7 = xml.createElement("PARAM");
            param7.setAttribute("pname", "CustomerID");
            param7.setAttribute("pval", map.get("customerId").toString());
            params.appendChild(param7);
        } else {
            Element param7 = xml.createElement("PARAM");
            param7.setAttribute("pname", "CustomerID");
            param7.setAttribute("pval", "0");
            params.appendChild(param7);
        }
        params.appendChild(param);
        params.appendChild(param1);
        params.appendChild(param2);
        params.appendChild(param3);
        params.appendChild(param4);
        params.appendChild(param5);
        if (map.get("date").equals("1")) {
            Element param6 = xml.createElement("PARAM");
            String bookingTime = map.get("bookingTime").toString();
            String[] split = bookingTime.split("/");
            System.out.println(split[0]);
            param6.setAttribute("pname", "BookingTime");
            param6.setAttribute("pval", split[0]);
            params.appendChild(param6);
        }
        String s = XmlUtil.toStr(xml, "gbk", false);
        log.info("华山预约接口输出前-->{}", s);
        SoapClient client = SoapClient.create(url)
                .setMethod("execXML", "")
                .setParam("in0", s);
        String send = client.send(true);
        Document document = XmlUtil.parseXml(send);
        String textContent = XmlUtil.transElements(document.getElementsByTagName("ns1:out")).get(0).getTextContent();
        JSONObject jsonObject = JSONUtil.xmlToJson(textContent);
        log.info("华山预约接口输出后-->{}", jsonObject);
        JSONObject o = null;
        if (map.get("date").equals("1")) {
            o = (JSONObject) jsonObject.get("HS.BOOKING.ConfirmBooking.BookingTime");
        } else {
            o = (JSONObject) jsonObject.get("HS.BOOKING.ConfirmBooking");
        }
        JSONObject oJSONObject = o.getJSONObject("PARAMS");
        // 这里 hutool 返回 JSONArray 节点，遍历
        Object paramArr = oJSONObject.get("PARAM");
        if (paramArr instanceof cn.hutool.json.JSONArray) {
            cn.hutool.json.JSONArray array = (cn.hutool.json.JSONArray) paramArr;
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                String pval = object.get("pval").toString();
                String pname = object.get("pname").toString();
                hashMap.put(pname, pval);
            }
        }
        return hashMap;
    }

    /**
     * 取消预约
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delReservation(Map<String, Object> map) {
        OfflineOderYue oderYue = this.oderYueMapper.selectById(map.get("id").toString());
        QueryWrapper<OfflineOderYue> wrapper = new QueryWrapper<>();
        wrapper.eq("id", map.get("id"));
        OfflineOderYue offlineOderYue = new OfflineOderYue();
        offlineOderYue.setIsStatus("INVALID");
        offlineOderYue.setIsStatusCode("INVALID");
        offlineOderYue.setDelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        this.oderYueMapper.update(offlineOderYue, wrapper);
        QueryWrapper<OfflineYue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", oderYue.getAboutId());
        OfflineYue yue = new OfflineYue();
        yue.setStatus("3");
        if (null != oderYue.getOrderId() && !"".equals(oderYue.getOrderId())) {
            QueryWrapper<OfflineOder> oderQueryWrapper = new QueryWrapper<>();
            oderQueryWrapper.eq("id", oderYue.getOrderId());
            OfflineOder oder = new OfflineOder();
            oder.setStatus("作废");
            this.mapper.update(oder, oderQueryWrapper);
        }
        return this.yueMapper.update(yue, queryWrapper) == 1;
    }

    /**
     * 回调地址
     */
    @Override
    public String findNotifyUrl(String payInfo) {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9mOPvaeobEfSerTsKS6FAkN7Soe7GlczX9cusrl9/Hlx7Scs3G8WuMbtNruj7bo" +
                "FS8e3GZmCOiONJkgEE+YtSrHrl2cdEHcz+hSHCS57pejLAMRLj/018nnHJxV2Cp7Mbq8MoVwpaSQrLbEFO6TGGvzUIfQ4grHkQpmQBfiFoDwIDAQAB";
        String outStr = "";
        try {
            String decode = URLDecoder.decode(payInfo, "UTF-8");
            byte[] inputByte = Base64.getDecoder().decode(decode);
            byte[] bytes = Base64.getDecoder().decode(publicKey);
            RSAPublicKey priKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new PKCS8EncodedKeySpec(bytes));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outStr;
    }

    /**
     * 支付短信的通知
     */
    @Override
    public String sendMessage(Map<String, Object> map) {
        Map<String, Object> hashMap = new HashMap<>();
        if ("1".equals(map.get("message"))) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(map.get("phone")).append(",").append(map.get("name")).append(",").append(map.get("url"));
            hashMap.put("msg", "【华山医院】尊敬的{$var}，您的订单支付链接为："+properties.getUrl()+"/{$var}，请点击支付。");
            hashMap.put("mobile", buffer);
            String send = SmsUtils.offlineSend(hashMap);
            log.info("发送短信结果-->{}", send);
            return send;
        } else {
            StringBuffer buffer = new StringBuffer();
            // TODO: 跨域依赖 - 等 sys 域迁入后 SecurityUtils.getLoginUser() 返回值包含 trueName/phone
            // SysUser user = SecurityUtils.getLoginUser();
            // String userName = user.getTrueName();
            // String phone = user.getPhone();
            String userName = String.valueOf(map.getOrDefault("userName", ""));
            String phone = String.valueOf(map.getOrDefault("phone", ""));
            buffer.append(phone).append(",").append(userName).append(",").append(map.get("name")).append(",").append(map.get("url"));
            hashMap.put("msg", "【华山医院】尊敬的{$var}，{$var}的订单支付链接为："+properties.getUrl()+"/{$var}，请点击支付。");
            hashMap.put("mobile", buffer);
            String send = SmsUtils.offlineSend(hashMap);
            log.info("发送短信结果-->{}", send);
            return send;
        }
    }

    /**
     * 导出功能
     */
    @Override
    public void orderYueOutExcel(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        transformationMap(map);
        QueryWrapper<OfflineOderYue> wrapper = isField(map);
        List<OfflineOderYue> list = this.oderYueMapper.selectList(wrapper);
        list.forEach(x -> filterCost(x));
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "订单管理(华东,华山).xlsx";
        ExcelUtils.Excel(request, response, list, OfflineOderYue.class, fileName);
    }

    private static void transformationMap(Map<String, Object> map) {
        if (null != map.get("districtName") && !"".equals(map.get("districtName"))) {
            switch (map.get("districtName").toString()) {
                case "华东":
                    map.put("district", "复旦大学附属华东医院");
                    break;
                case "华山":
                    map.put("district", "复旦大学附属华山医院(总院)");
                    break;
                case "华山(江苏路)":
                    map.put("district", "复旦大学附属华山医院(江苏路分部)");
                    break;
                case "华山(传染大楼)":
                    map.put("district", "复旦大学附属华山医院(传染科大楼)");
                    break;
                case "华山(西)":
                    map.put("district", "复旦大学附属华山医院(西院)");
                    break;
            }
        }
    }

    /**
     * 订单管理中的字段判断
     */
    private QueryWrapper<OfflineOderYue> isField(Map<String, Object> map) {
        QueryWrapper<OfflineOderYue> wrapper = new QueryWrapper<>();
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            wrapper.like("phone", map.get("phone"));
        }
        if (null != map.get("district") && !"".equals(map.get("district"))) {
            wrapper.eq("district_name", map.get("district"));
        }
        if (null != map.get("groupName") && !"".equals(map.get("groupName"))) {
            wrapper.eq("group_name", map.get("groupName"));
        }
        if (null != map.get("codeName") && !"".equals(map.get("codeName"))) {
            wrapper.eq("code_name", map.get("codeName"));
        }
        if (null != map.get("staffName") && !"".equals(map.get("staffName"))) {
            wrapper.like("staff_name", map.get("staffName"));
        }
        if (null != map.get("treatmentType") && !"".equals(map.get("treatmentType"))) {
            wrapper.like("treatment_type", map.get("treatmentType"));
        }
        if (null != map.get("schedulingDate") && !"".equals(map.get("schedulingDate"))) {
            wrapper.eq("scheduling_date", map.get("schedulingDate"));
        }
        if (null != map.get("isStatus") && !"".equals(map.get("isStatus"))) {
            wrapper.eq("is_status", map.get("isStatus"));
        }
        return wrapper;
    }

    /**
     * 重新发送挂号成功的短信给用户
     */
    @Override
    public String resendMessage(Map<String, Object> map) {
        QueryWrapper<OfflineOderYue> yueQueryWrapper = new QueryWrapper<>();
        yueQueryWrapper.eq("id", map.get("id"));
        OfflineOderYue offlineOderYue = this.oderYueMapper.selectOne(yueQueryWrapper);
        OfflineDistrict offlineDistrict = this.oderYueMapper.findDistrictName(offlineOderYue.getDistrictName());
        Map<String, Object> hashMap = new HashMap<>();
        StringBuffer buffer = new StringBuffer();
        buffer.append(offlineOderYue.getPhone()).append(",")
                .append(offlineOderYue.getName()).append(",")
                .append(offlineOderYue.getBookingId()).append(",")
                .append(offlineOderYue.getSchedulingDate()).append(",")
                .append(offlineOderYue.getDistrictName()).append(",")
                .append(offlineOderYue.getCodeName()).append(",")
                .append(offlineOderYue.getStaffName()).append(",")
                .append(offlineOderYue.getGroupName()).append(",")
                .append(offlineOderYue.getSchedulingDate()).append(" ").append(offlineOderYue.getYueTime()).append(",")
                .append(offlineDistrict.getDistrictAddress()).append(offlineOderYue.getFloor()).append(",")
                .append(offlineOderYue.getYueId());
        hashMap.put("msg", "【华山医院】尊敬的{$var}，您的订单{$var}已预约{$var}{$var}的{$var}{$var}{$var}门诊。请于{$var}携带本人身份证原件或户口本原件(限儿童)到{$var}挂号，就诊号{$var}，超过预约时段不保留号源。就诊时请带好所有既往就诊资料。请保留短信以做凭证，转发短信无效。如您不能按约就诊需提前两个工作日撤销，否则将记录失约，半年内失约两次暂停预约资格6个月。一个月内订单只能取消3次。请保持手机畅通，关注预约就诊信息。最终就诊以医院当日信息为准。");
        hashMap.put("mobile", buffer);
        String send = SmsUtils.offlineSend(hashMap);
        log.info("重新发送短信结果:{}", send);
        return send;
    }

    /**
     * 查询订单列表 数值转换
     */
    private void filterCost(OfflineOderYue x) {
        if (x.getDistrictName() != null) {
            switch (x.getDistrictName()) {
                case "总院":
                    x.setDistrictName("复旦大学附属华山医院(总院)");
                    break;
                case "江苏路分部":
                    x.setDistrictName("复旦大学附属华山医院(江苏路分部)");
                    break;
                case "传染科大楼":
                    x.setDistrictName("复旦大学附属华山医院(传染科大楼)");
                    break;
                case "西院":
                    x.setDistrictName("复旦大学附属华山医院(西院)");
                    break;
            }
        }
        if ("1".equals(x.getTreatmentType())) {
            x.setTreatmentType("自费");
        } else if ("2".equals(x.getTreatmentType())) {
            x.setTreatmentType("医保卡");
        }
        String schedulingDate = x.getSchedulingDate();
        String schedulingType = x.getSchedulingType();
        if (null == schedulingDate || "".equals(schedulingDate) &&
                null == schedulingType || "".equals(schedulingType)) {
            schedulingDate = "";
            schedulingType = "";
        }
        if ("1".equals(schedulingType)) {
            schedulingType = "上午";
        } else if ("2".equals(schedulingType)) {
            schedulingType = "下午";
        }
        String s = schedulingDate + schedulingType;
        x.setDate(s);
        if (x.getIsStatus() != null) {
            switch (x.getIsStatus()) {
                case "RESERVED":
                    x.setIsStatus("已预约");
                    break;
                case "PAID":
                    x.setIsStatus("待支付");
                    break;
                case "SUCCESS":
                    x.setIsStatus("挂号成功");
                    break;
                case "FAIL":
                    x.setIsStatus("挂号失败");
                    break;
                case "CLOSE":
                    x.setIsStatus("停诊");
                    break;
                case "INVALID":
                    x.setIsStatus("已取消");
                    break;
                case "PROGRESS":
                    x.setIsStatus("退款中");
                    break;
                case "REFUND":
                    x.setIsStatus("退款成功");
                    break;
                case "VISITED":
                    x.setIsStatus("已就诊");
                    break;
            }
        }
    }

    /**
     * 创建订单数据
     */
    private OfflineOderYue insertOderYue(Map<String, Object> map) {
        OfflineDistrict offlineDistrict = this.oderYueMapper.findDistrictName(map.get("districtName").toString());
        OfflineOderYue oderYue = new OfflineOderYue();
        if (null != map.get("orderId") && !"".equals(map.get("orderId"))) {
            oderYue.setOrderId(map.get("orderId").toString());
        }
        oderYue.setName(map.get("customerName").toString());
        oderYue.setCertificates(map.get("customerNo").toString());
        oderYue.setCertificatesType(map.get("customerType").toString());
        oderYue.setPhone(map.get("customerTel").toString());
        oderYue.setDistrictId(offlineDistrict.getDistrictId());
        oderYue.setDistrictName(map.get("districtName").toString());
        if (null != map.get("groupId") && !"".equals(map.get("groupId"))) {
            oderYue.setGroupId(map.get("groupId").toString());
        }
        if (null != map.get("groupId") && !"".equals(map.get("groupId"))) {
            if ("2".equals(map.get("groupId").toString())) {
                oderYue.setGroupName("专家门诊");
            } else if ("13".equals(map.get("groupId").toString())) {
                oderYue.setGroupName("特需全自费");
            } else {
                oderYue.setGroupName(map.get("groupName").toString());
            }
        }
        if (null != map.get("codeId") && !"".equals(map.get("codeId"))) {
            oderYue.setCodeId(map.get("codeId").toString());
        }
        if (null != map.get("codeName") && !"".equals(map.get("codeName"))) {
            oderYue.setCodeName(map.get("codeName").toString());
        }
        if (null != map.get("staffId") && !"".equals(map.get("staffId"))) {
            oderYue.setStaffId(map.get("staffId").toString());
        }
        if (null != map.get("staffName") && !"".equals(map.get("staffName"))) {
            oderYue.setStaffName(map.get("staffName").toString());
        }
        if ("1".equals(map.get("date"))) {
            oderYue.setSchedulingDate(map.get("schedulingDate").toString());
            if ("上午".equals(map.get("schedulingType").toString())) {
                oderYue.setSchedulingType("1");
            } else if ("下午".equals(map.get("schedulingType").toString())) {
                oderYue.setSchedulingType("2");
            } else {
                oderYue.setSchedulingType(map.get("schedulingType").toString());
            }
        }
        if (null != map.get("money") && !"".equals(map.get("money"))) {
            oderYue.setMoney(map.get("money").toString());
        }
        oderYue.setTreatmentType(map.get("customer").toString());
        if (null != map.get("customerId") && !"".equals(map.get("customerId"))) {
            oderYue.setTreatmentName(map.get("customerId").toString());
        }
        oderYue.setMessage(map.get("message").toString());
        oderYue.setDecisionTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return oderYue;
    }

    /**
     * create 预约后的订单
     */
    private OfflineOderYue createOderYue(OfflineConfirmBooking booking, Map<String, Object> map) {
        OfflineDistrict offlineDistrict = this.oderYueMapper.findDistrictName(map.get("districtName").toString());
        OfflineOderYue oderYue = new OfflineOderYue();
        if (!"".equals(map.get("orderId")) && null != map.get("orderId")) {
            oderYue.setOrderId(map.get("orderId").toString());
        }
        oderYue.setName(map.get("customerName").toString());
        oderYue.setCertificates(map.get("customerNo").toString());
        oderYue.setCertificatesType(map.get("customerType").toString());
        oderYue.setPhone(map.get("customerTel").toString());
        oderYue.setDistrictId(offlineDistrict.getDistrictId());
        oderYue.setDistrictName("复旦大学附属华东医院");
        oderYue.setGroupId(map.get("groupId").toString());
        oderYue.setGroupName(map.get("groupName").toString());
        oderYue.setCodeId(map.get("codeId").toString());
        oderYue.setCodeName(map.get("codeName").toString());
        oderYue.setStaffId(map.get("staffId").toString());
        oderYue.setStaffName(map.get("staffName").toString());
        oderYue.setMessage(map.get("message").toString());
        oderYue.setIsStatus("SUCCESS");
        oderYue.setIsStatusCode("SUCCESS");
        oderYue.setSchedulingDate(booking.getClassDate());
        if ("上午".equals(booking.getDayType())) {
            oderYue.setSchedulingType("1");
        } else if ("下午".equals(booking.getDayType())) {
            oderYue.setSchedulingType("2");
        } else {
            oderYue.setSchedulingType(map.get("schedulingType").toString());
        }
        oderYue.setMoney(booking.getPrice());
        oderYue.setTreatmentType("2");
        if (null != map.get("customerId") && !"".equals(map.get("customerId"))) {
            oderYue.setTreatmentName(map.get("customerId").toString());
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        oderYue.setDecisionTime(date);
        oderYue.setYueTime(booking.getPeriod());
        oderYue.setFloor(booking.getAddress());
        oderYue.setBookingId(booking.getBookingId());
        return oderYue;
    }

    /**
     * 获取 订单后的数据
     */
    private OfflineConfirmBooking createConfirmBooking(JSONObject object) {
        OfflineConfirmBooking booking = new OfflineConfirmBooking();
        booking.setBookingId(object.get("BookingId").toString());
        booking.setNodeName(object.get("NodeName").toString());
        booking.setClassId(object.get("ClassId").toString());
        booking.setEntityId(object.get("EntityId").toString());
        booking.setEntityName(object.get("EntityName").toString());
        booking.setClassDate(object.get("ClassDate").toString());
        booking.setDayType(object.get("DayType").toString());
        booking.setPeriod(object.get("PeriodId").toString());
        booking.setPeriod(object.get("Period").toString());
        booking.setPostion(object.get("Postion").toString());
        booking.setAddress(object.get("Address").toString());
        booking.setServiceType(object.get("ServiceType").toString());
        booking.setPrice(object.get("Price").toString());
        booking.setMessage(object.get("Message").toString());
        return booking;
    }

}