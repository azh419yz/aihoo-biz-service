package com.aihoo.domain.prescription.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aihoo.common.PageResult;
import com.aihoo.domain.prescription.model.entity.HosPreDrugOrder;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.prescription.model.mapper.HosPreDrugMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugMapper;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.prescription.model.vo.HosPreDrugVo;
import com.aihoo.domain.prescription.properties.PrescriptionProperties;
import com.aihoo.domain.consultation.model.entity.MdtOrder;
import com.aihoo.domain.consultation.model.mapper.MdtOrderMapper;
import com.aihoo.domain.payment.model.entity.Order;
import com.aihoo.domain.payment.model.mapper.OrderMapper;
import com.aihoo.domain.prescription.service.HosPreDrugService;
import com.aihoo.domain.prescription.util.HospitalPrescriptionUtil;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.mapper.RevisitOrderMapper;
import com.aihoo.domain.visit.service.VisitOrderService;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.mapper.DiceMapper;
import com.aihoo.exception.BizException;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname HosPreDrugServiceImpl
 * @Description hf
 * @Date 2020/9/24 15:02
 * @Created by ad
 */
@Service
public class HosPreDrugServiceImpl extends ServiceImpl<HosPrescriptionDrugMapper, HosPrescriptionDrug>
        implements HosPreDrugService {

    @Resource
    private HosPreDrugMapper hosPreDrugMapper;
    @Resource
    private HosPrescriptionMapper hosPrescriptionMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private RevisitOrderMapper revisitOrderMapper;
    @Resource
    private VisitOrderService visitOrderService;
    @Resource
    private MdtOrderMapper mdtOrderMapper;
    // 当前 stub：真实实现需 shared-kernel 提供签名工具。
    @Autowired
    private HospitalPrescriptionUtil hospitalPrescriptionUtil;
    @Resource
    private DiceMapper diceMapper;
    @Resource
    private PrescriptionProperties prescriptionProperties;

    @Override
    public PageResult drugList(Map<String, Object> map) {
        int limit = Integer.parseInt(map.get("limit").toString());
        int page = Integer.parseInt(map.get("page").toString());
        int startNum = (page - 1) * limit;
        JSONArray jsonArray = new JSONArray();
        map.put("startNum", startNum);
        map.put("limit", limit);
        List<HosPreDrugVo> hosPreDrugVoList = hosPreDrugMapper.drugList(map);
        int count = hosPreDrugMapper.getCount(map);
        if (CollectionUtils.isEmpty(hosPreDrugVoList)) {
            return new PageResult(jsonArray, count);
        }
        for (HosPreDrugVo hosPreDrugVo : hosPreDrugVoList) {
            JSONObject jsonObject = new JSONObject();
            String orderType = getOrderType(hosPreDrugVo.getVisitMdtNum());
            if ("REVISIT".equals(orderType)) {
                HosRevisit hosRevisit = revisitOrderMapper.selectOne(new QueryWrapper<HosRevisit>().eq("order_num", hosPreDrugVo.getVisitMdtNum()));
                if (null == hosRevisit) {
                    jsonObject.put("visitMdtId", "");
                }
                jsonObject.put("visitMdtId", hosRevisit.getId());
            } else if ("VISIT".equals(orderType)) {
                HosVisit hosVisit = visitOrderService.getOne(new QueryWrapper<HosVisit>().eq("order_num", hosPreDrugVo.getVisitMdtNum()));
                if (null == hosVisit) {
                    jsonObject.put("visitMdtId", "");
                }
                jsonObject.put("visitMdtId", hosVisit.getId());
            } else {
                MdtOrder mdtOrder = mdtOrderMapper.selectOne(new QueryWrapper<MdtOrder>().eq("order_num", hosPreDrugVo.getVisitMdtNum()));
                if (null == mdtOrder) {
                    jsonObject.put("visitMdtId", "");
                }
                jsonObject.put("visitMdtId", mdtOrder.getId());
            }
            jsonObject.put("id", hosPreDrugVo.getId());
            jsonObject.put("visitMdtNum", hosPreDrugVo.getVisitMdtNum());
            jsonObject.put("orderNum", hosPreDrugVo.getOrderNum());
            jsonObject.put("name", hosPreDrugVo.getName());
            jsonObject.put("mobile", hosPreDrugVo.getMobile());

            String province = StrUtil.nullToEmpty(hosPreDrugVo.getProvince());
            String city = StrUtil.nullToEmpty(hosPreDrugVo.getCity());
            String district = StrUtil.nullToEmpty(hosPreDrugVo.getDistrict());
            String address = StrUtil.nullToEmpty(hosPreDrugVo.getAddress());

            String addressSum = province + "" + "" + city + "" + district + "" + address;

            jsonObject.put("address", addressSum);
            jsonObject.put("type", hosPreDrugVo.getType());
            jsonObject.put("totalPrice", hosPreDrugVo.getTotalPrice());
            jsonObject.put("status", hosPreDrugVo.getStatus());
            jsonArray.add(jsonObject);
        }
        return new PageResult(jsonArray, count);
    }

    @Override
    public JSONObject getDrugDetails(Map<String, Object> map) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HosPreDrugOrder hosPreDrugOrder = hosPreDrugMapper.getDrugDetails(map.get("id").toString());
        if (null == hosPreDrugOrder) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "该药品订单不存在");
            return jsonObject;
        }
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("order_num", hosPreDrugOrder.getOrderNum()));
        if (null == order) {
            jsonObject.put("waterNumber", "");
        } else {
            jsonObject.put("waterNumber", order.getPayOrderNum());
        }

        jsonObject.put("id", hosPreDrugOrder.getId());
        jsonObject.put("name", hosPreDrugOrder.getName());
        jsonObject.put("mobile", hosPreDrugOrder.getMobile());
        jsonObject.put("address", hosPreDrugOrder.getAddress());
        jsonObject.put("status", hosPreDrugOrder.getStatus());
        jsonObject.put("orderNum", hosPreDrugOrder.getOrderNum());
        jsonObject.put("createTimeDrug", hosPreDrugOrder.getCreateTime());
        jsonObject.put("payTimeDrug", hosPreDrugOrder.getPayTime());
        jsonObject.put("shippendStartTime", hosPreDrugOrder.getShippendStartTime());
        jsonObject.put("shippendEndTime", hosPreDrugOrder.getShippendEndTime());
        HosPrescription hosPrescription = hosPreDrugOrder.getHosPrescription();
        if (null == hosPrescription) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "该药品不关联处方请核实");
            return jsonObject;
        }
        String orderType = getOrderType(hosPrescription.getVisitMdtNum());
        if ("REVISIT".equals(orderType)) {
            HosRevisit hosRevisit = revisitOrderMapper.selectOne(new QueryWrapper<HosRevisit>().eq("order_num", hosPrescription.getVisitMdtNum()));
            if (null == hosRevisit) {
                jsonObject.put("is_succ", 0);
                jsonObject.put("msg", "该复诊订单不存在");
                return jsonObject;
            }
            jsonObject.put("visitMdtId", hosRevisit.getId());
        } else if ("VISIT".equals(orderType)) {
            HosVisit hosVisit = visitOrderService.getOne(new QueryWrapper<HosVisit>().eq("order_num", hosPrescription.getVisitMdtNum()));
            if (null == hosVisit) {
                jsonObject.put("is_succ", 0);
                jsonObject.put("msg", "该在线复诊订单不存在");
                return jsonObject;
            }
            jsonObject.put("visitMdtId", hosVisit.getId());
        } else {
            MdtOrder mdtOrder = mdtOrderMapper.selectOne(new QueryWrapper<MdtOrder>().eq("order_num", hosPrescription.getVisitMdtNum()));
            if (null == mdtOrder) {
                jsonObject.put("is_succ", 0);
                jsonObject.put("msg", "该MDT订单不存在");
                return jsonObject;
            }
            jsonObject.put("visitMdtId", mdtOrder.getId());
        }
        jsonObject.put("prescriptionId", hosPrescription.getId());
        jsonObject.put("hoSickName", hosPrescription.getName());
        jsonObject.put("type", hosPrescription.getType());
        jsonObject.put("hoSickMobile", hosPrescription.getMobile());
        jsonObject.put("visitMdtNum", hosPrescription.getVisitMdtNum());
        jsonObject.put("endTime", hosPrescription.getEndTime());
        Dict dict = diceMapper.selectOne(new QueryWrapper<Dict>().eq("type", "PAY_TYPE").eq("code", hosPrescription.getPayType()));
        if (null == dict) {
            jsonObject.put("payType", "");
        } else {
            jsonObject.put("payType", dict.getName());
        }


        jsonObject.put("createTime", hosPrescription.getCreateTime());
        jsonObject.put("totalPrice", hosPrescription.getTotalPrice());
        jsonObject.put("payTime", hosPrescription.getPayTime());
        JSONArray jsonArray = new JSONArray();
        List<HosPrescriptionDrug> hosPrescriptionDrugs = hosPreDrugOrder.getHosPrescriptionDrugs();
        if (CollectionUtils.isEmpty(hosPrescriptionDrugs)) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "该处方下无药品信息，请核实");
            return jsonObject;
        }
        int drugSumNumber = 0;
        double priceSum = 0;
        for (HosPrescriptionDrug hosPrescriptionDrug : hosPrescriptionDrugs) {
            JSONObject drugObject = new JSONObject();
            drugObject.put("drugName", hosPrescriptionDrug.getName());
            drugObject.put("drugSize", hosPrescriptionDrug.getSize());
            drugObject.put("drugPrice", hosPrescriptionDrug.getPrice());
            drugObject.put("drugNumber", hosPrescriptionDrug.getNumber());
            drugObject.put("drugId", hosPrescriptionDrug.getId());
            Double price = Double.valueOf(hosPrescriptionDrug.getPrice());
            Integer number = Integer.valueOf(hosPrescriptionDrug.getNumber());
            String sumPrice = String.valueOf(number * price);
            drugObject.put("drugPriceSum", sumPrice);
            jsonArray.add(drugObject);
            drugSumNumber += number;
            priceSum += Double.valueOf(sumPrice);
        }
        jsonObject.put("drugSumNumber", String.valueOf(drugSumNumber));
        jsonObject.put("priceSum", String.valueOf(priceSum));
        jsonObject.put("freight", "0");
        HosPrescription hosPrescriptionDeliveryStatus = hosPrescriptionMapper.selectOne(new QueryWrapper<HosPrescription>().eq("id", hosPreDrugOrder.getHosPrescriptionId()));
        if (null == hosPrescriptionDeliveryStatus) {
            jsonObject.put("is_succ", 0);
            jsonObject.put("msg", "该处方订单不存在");
            return jsonObject;
        }
        JSONObject hosPrescriptionDeliveryStatusJson = hospitalPrescriptionUtil.prescriptionLogistics(prescriptionProperties.getReqsUrl(), prescriptionProperties.getAppId(), prescriptionProperties.getAppsecret(), hosPrescriptionDeliveryStatus.getOrderNum(), null, "北京合偶平方互联网医院");

        jsonObject.put("deliveryList", hosPrescriptionDeliveryStatusJson.get("result"));


//        HosPrescriptionDrug hosPrescriptionDrug = hosPrescriptionDrugs.get(4);
//        HosPrescriptionDrug hosPrescriptionDrug1 = hosPrescriptionDrugs.get(0);
        jsonObject.put("drugList", jsonArray);
        jsonObject.put("is_succ", 1);
        return jsonObject;
    }


    @Override
    public Object getDrugDeliveryStatus(Map<String, Object> map) throws Exception {
        HosPreDrugOrder hosPreDrugOrder = hosPreDrugMapper.selectOne(new QueryWrapper<HosPreDrugOrder>().eq("id", map.get("id").toString()));
        if (null == hosPreDrugOrder) {

        }
        HosPrescription hosPrescriptionDeliveryStatus = hosPrescriptionMapper.selectOne(new QueryWrapper<HosPrescription>().eq("id", hosPreDrugOrder.getHosPrescriptionId()));
        if (null == hosPrescriptionDeliveryStatus) {

        }
        JSONObject hosPrescriptionDeliveryStatusJson = hospitalPrescriptionUtil.prescriptionLogistics(prescriptionProperties.getReqsUrl(), prescriptionProperties.getAppId(), prescriptionProperties.getAppsecret(), hosPrescriptionDeliveryStatus.getOrderNum(), null, "北京合偶平方互联网医院");
        Object result = hosPrescriptionDeliveryStatusJson.get("result");
        return result;
    }

    public String getOrderType(String orderNum) {
        switch (orderNum.substring(0, 1)) {
            case "R":
                return "REVISIT";
            case "M":
                return "MDT";
            case "V":
                return "VISIT";
            default:
                throw new BizException("请核对订单号再操作!");
        }
    }

}
