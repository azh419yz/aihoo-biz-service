package com.aihoo.domain.payment.service;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.payment.dto.MdtOrderReportVo;

import java.util.List;
import java.util.Map;

public interface MdtOrderService {
    List bill(Map<String, String> map);

    JsonResult list(Map<String, String> map);

    JsonResult count(Map<String, String> map);

    JsonResult details(Map<String, String> map);

    JsonResult receivingOrders(Map<String, String> map);

    JsonResult doctorAgree(Map<String, Object> map);

    List<Map<String, Object>> medicalHistory(String orderId);

    JsonResult auditReview(Map<String, Object> map);

    JsonResult mdtReport(String orderId, String medicalHistorySummary, String consultationSummary, String diagnosisResults,
                         String treatmentPlan, String imgPath, String demand, String checkup);

    JsonResult sign(Map<String, String> param);

    MdtOrderReportVo orderReport(String orderId);

    JsonResult prescriptionSign(Map<String, String> map);

    JsonResult setPrescription(Map<String, Object> map);

    JsonResult prescriptionDetails(Map<String, String> map);

    JsonResult commitPrescription(Map<String, String> map);

    JsonResult prescriptionStatus(Map<String, String> map);
}
