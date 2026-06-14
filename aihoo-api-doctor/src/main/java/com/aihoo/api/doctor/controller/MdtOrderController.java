package com.aihoo.api.doctor.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.JsonResult;
import com.aihoo.exception.BizException;
import com.alibaba.fastjson2.JSONObject;
import com.aihoo.domain.payment.dto.MdtOrderReportVo;
import com.aihoo.domain.payment.service.MdtOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: aihoo-root
 * @description: 会诊接口
 * @author: Mr.Li
 * @create: 2021-01-07 17:02
 **/
@Controller
@RequestMapping("/api/v1/mdtOrder")
@Slf4j
public class MdtOrderController {

    @Resource
    private MdtOrderService mdtOrderService;

    /**
     * 账单接口
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/bill")
    public JsonResult bill(@RequestBody Map<String, String> map) {
        try {
            List bill = mdtOrderService.bill(map);
            return JsonResult.ok().put("data", bill);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 获取不同状态下的订单总数
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/count")
    public JsonResult count(@RequestBody Map<String, String> map) {
        try {
            return mdtOrderService.count(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 获取根据订单状态查询订单列表
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public JsonResult list(@RequestBody Map<String, String> map) {
        try {
            if (StrUtil.isBlank(map.get("type"))) {
                return JsonResult.error("type不能为空");
            }
            return mdtOrderService.list(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * MDT订单详情
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/details")
    public JsonResult details(@RequestBody Map<String, String> map) {
        try {
            if (StrUtil.isBlank(map.get("orderId"))) {
                return JsonResult.error("orderId不能为空");
            }
            return mdtOrderService.details(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 医生接单
     *
     * @param map 入参
     * @return {}
     */
    @ResponseBody
    @PostMapping("receivingOrders")
    public JsonResult receivingOrders(@RequestBody Map<String, String> map) {
        try {
            if (StrUtil.isBlank(map.get("orderId"))) {
                return JsonResult.error("orderId不能为空");
            }
            return mdtOrderService.receivingOrders(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("系统繁忙");
        }
    }

    /**
     * 时间确认
     *
     * @param map 入参
     * @return {}
     */
    @ResponseBody
    @PostMapping("/doctorAgree")
    public JsonResult doctorAgree(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("orderId") || StrUtil.isBlank(map.get("orderId").toString())) {
                return JsonResult.error("orderId不能为空");
            }
            Object isAgree = map.get("isAgree");
            if (null == map.get("isAgree") || StrUtil.isBlank(isAgree.toString())) {
                return JsonResult.error("isAgree不能为空");
            }
            if (!isAgree.toString().equals("0") && !isAgree.toString().equals("1") && !isAgree.toString().equals("2")) {
                return JsonResult.error("isAgree只能为 1/2");
            }
            return mdtOrderService.doctorAgree(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 根据id查询 病史资料详情
     *
     * @param map 入参
     * @return {}
     */
    @ResponseBody
    @PostMapping("/medicalHistory")
    public JsonResult medicalHistory(@RequestBody Map<String, String> map) {
        try {
            if (StrUtil.isBlank(map.get("orderId"))) {
                return JsonResult.error("必须携带会诊订单id");
            }
            List<Map<String, Object>> res = mdtOrderService.medicalHistory(map.get("orderId"));
            return JsonResult.ok().put("data", res);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("根据id查询病史资料详情出错");
        }
    }

    /**
     * 病史资料资料审核
     *
     * @param map auditResult 审核结果 1-同意 2-驳回
     * @return
     */
    @ResponseBody
    @PostMapping("/auditReview")
    public JsonResult auditReview(@RequestBody Map<String, Object> map) {
        try {
            Object orderId = map.get("orderId");
            if (null == orderId || StrUtil.isBlank(orderId.toString())) {
                return JsonResult.error("orderId不能空");
            }
            Object auditResult = map.get("auditResult");
            if (null == auditResult || StrUtil.isBlank(auditResult.toString())) {
                return JsonResult.error("auditResult不能空");
            }
            if (!auditResult.toString().equals("0") && !auditResult.toString().equals("1") && !auditResult.toString().equals("2")) {
                return JsonResult.error("auditResult只能为 1/2");
            }
            return mdtOrderService.auditReview(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 会诊报告
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/report")
    public JsonResult mdtReport(@RequestBody Map<String, Object> map) {
        log.info("保存会诊报告入参param={}", map);
        boolean isImage;//是否是图片
        try {
            if (null == map.get("orderId") || StrUtil.isBlank(map.get("orderId").toString())) {
                return JsonResult.error(500, "orderId CANNOT BE EMPTY");
            }
            if (null == map.get("isImage") || StrUtil.isBlank(map.get("isImage").toString())) {
                return JsonResult.error(500, "isImage CANNOT BE EMPTY");
            }
            if ("0".equals(map.get("isImage").toString())) {
                isImage = false;
            } else if ("1".equals(map.get("isImage").toString())) {
                isImage = true;
            } else {
                return JsonResult.error(500, "imgPath CANNOT BE EMPTY");
            }
            String orderId = map.get("orderId").toString();//MDT订单编号
            String imgPath = null;//会诊报告图片
            String demand = "";//会诊要求
            String checkup = "";//检查报告
            String medicalHistorySummary = null;//病史摘要
            String consultationSummary = null;//会诊摘要
            String diagnosisResults = null;//诊断结果
            String treatmentPlan = null;//治疗方案
            if (isImage) {
                if (null == map.get("imgPath") || StrUtil.isBlank(map.get("imgPath").toString())) {
                    return JsonResult.error(500, "imgPath CANNOT BE EMPTY");
                }
                imgPath = map.get("imgPath").toString();//病史摘要
                if (ObjectUtil.isNotEmpty(map.get("medicalHistorySummary"))) {
                    medicalHistorySummary = map.get("medicalHistorySummary").toString();//病史摘要
                }
                if (ObjectUtil.isNotEmpty(map.get("consultationSummary"))) {
                    consultationSummary = map.get("consultationSummary").toString();//会诊摘要
                }
                if (ObjectUtil.isNotEmpty(map.get("diagnosisResults"))) {
                    diagnosisResults = map.get("diagnosisResults").toString();//诊断结果
                }
                if (ObjectUtil.isNotEmpty(map.get("treatmentPlan"))) {
                    treatmentPlan = map.get("treatmentPlan").toString();//治疗方案
                }
            } else {
                if (null == map.get("medicalHistorySummary") || StrUtil.isBlank(map.get("medicalHistorySummary").toString())) {
                    return JsonResult.error(500, "medicalHistorySummary CANNOT BE EMPTY");
                }
                if (null == map.get("consultationSummary") || StrUtil.isBlank(map.get("consultationSummary").toString())) {
                    return JsonResult.error(500, "consultationSummary CANNOT BE EMPTY");
                }
                if (null == map.get("diagnosisResults") || StrUtil.isBlank(map.get("diagnosisResults").toString())) {
                    return JsonResult.error(500, "diagnosisResults CANNOT BE EMPTY");
                }
                if (null == map.get("treatmentPlan") || StrUtil.isBlank(map.get("treatmentPlan").toString())) {
                    return JsonResult.error(500, "treatmentPlan CANNOT BE EMPTY");
                }
                medicalHistorySummary = map.get("medicalHistorySummary").toString();//病史摘要
                consultationSummary = map.get("consultationSummary").toString();//会诊摘要
                diagnosisResults = map.get("diagnosisResults").toString();//诊断结果
                treatmentPlan = map.get("treatmentPlan").toString();//治疗方案
                if (map.containsKey("demand")) {
                    demand = map.get("demand").toString();
                }
                if (map.containsKey("checkup")) {
                    checkup = map.get("checkup").toString();
                }
            }
            return mdtOrderService.mdtReport(orderId, medicalHistorySummary, consultationSummary, diagnosisResults,
                    treatmentPlan, imgPath, demand, checkup);
        } catch (Exception e) {
            return JsonResult.error(500, "保存会诊报告失败");
        }
    }

    /**
     * 会诊医生会珍报告签章
     *
     * @param param 入参
     *              orderId订单id
     *              reportId报告id
     * @return {}
     */
    @ResponseBody
    @PostMapping("/sign")
    public JsonResult sign(@RequestBody Map<String, String> param) {
        String orderId = param.get("orderId");
        if (StrUtil.isBlank(orderId)) {
            return JsonResult.error("处方id为NULL");
        }
        try {
            return mdtOrderService.sign(param);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 查询会诊报告
     */
    @ResponseBody
    @PostMapping("orderReport")
    public JsonResult orderReport(@RequestBody Map<String, Object> map) {
        try {
            if (null == map.get("orderId") || "".equals(map.get("orderId"))) {
                return JsonResult.error("会诊id不能为空");
            }
            MdtOrderReportVo mdtOrderReportVo = mdtOrderService.orderReport(map.get("orderId").toString());
            if (null == mdtOrderReportVo) {
                return JsonResult.ok().put("data", new JSONObject());
            }
            return JsonResult.ok().put("data", mdtOrderReportVo);
        } catch (BizException e) {
            e.printStackTrace();
            return JsonResult.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("查询会诊报告失败!");
        }
    }

    /**
     * 会诊开处方提交签名接口
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/prescriptionSign")
    public JsonResult prescriptionSign(@RequestBody Map<String, String> map) {
        String id = map.get("orderId");
        if (StrUtil.isBlank(id)) {
            return JsonResult.error("订单id为空");
        }
        try {
            return mdtOrderService.prescriptionSign(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 开处方接口
     * <p>
     * {
     * "orderId":"1",
     * "drugs":[{"drugId":"7","freqMedCode":"Prn","routeAdmiCode":"922","medicalCertificate":"","number":"2","useDay":"3","dosage":"1.5克"}]
     * }
     *
     * @param map id复诊订单id diseaseCode疾病list drugCode药品list（drugId药品id，freqMedCode用法，routeAdmiCode频次）
     * @return
     */
    @ResponseBody
    @PostMapping("/setPrescription")
    public JsonResult setPrescription(@RequestBody Map<String, Object> map) {
        Object orderId = map.get("orderId");//id
        Object drugs = map.get("drugs");//药品
        if (null == orderId) {
            return JsonResult.error("订单id为空");
        } else if (null == drugs) {
            return JsonResult.error("drugs为null");
        }
        try {
            return mdtOrderService.setPrescription(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 处方查询接口
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/prescriptionDetails")
    public JsonResult prescriptionCancel(@RequestBody Map<String, String> map) {
        String id = map.get("orderId");//id
        if (StrUtil.isBlank(id)) {
            return JsonResult.error("订单id为空");
        }
        try {
            return mdtOrderService.prescriptionDetails(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 二次提交处方
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/commitPrescription")
    public JsonResult commitPrescription(@RequestBody Map<String, String> map) {
        String id = map.get("orderId");//id
        if (StrUtil.isBlank(id)) {
            return JsonResult.error("订单id为空");
        }
        try {
            return mdtOrderService.commitPrescription(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }

    /**
     * 二次提交处方
     *
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/prescriptionStatus")
    public JsonResult prescriptionStatus(@RequestBody Map<String, String> map) {
        String id = map.get("orderId");//id
        if (StrUtil.isBlank(id)) {
            return JsonResult.error("订单id为空");
        }
        try {
            return mdtOrderService.prescriptionStatus(map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error();
        }
    }
}
