package com.aihoo.api.doctor.app.service;

import com.aihoo.common.JsonResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.request.PrescriptionQueryRequest;
import com.aihoo.api.doctor.app.controller.request.SavePrescriptionRequest;
import com.aihoo.api.doctor.app.controller.request.SearchRecentPreRequest;
import com.aihoo.api.doctor.app.controller.request.WithdrawPrescriptionRequest;
import com.aihoo.api.doctor.app.controller.vo.RecentPreVo;
import com.aihoo.api.doctor.app.model.Disease;
import com.aihoo.api.doctor.app.model.Drug;
import com.aihoo.api.doctor.app.model.HosPrescription;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface PrescriptionService extends IService<HosPrescription> {
    List<Drug> drugList(Map<String, String> map);

    List<Disease> diseaseList(Map<String, Object> map);

    JsonResult sign(Map<String, String> map);

    JsonResult savePrescription(Map<String, Object> map);

    JsonResult commitPrescription(Map<String, String> map);

    JsonResult prescriptionDetails(Map<String, String> map);

    JsonResult getPrescriptionList(Map<String, String> map);

    JsonResult taboo();

    JsonResult getPrescriptionStatus(Map<String, String> map);

    Map<String, Object> getPrescription(Map<String, String> map);

    long countByDoctorUserId(String doctorUserId);

    RecentPreVo savePrescription(SavePrescriptionRequest request);

    RecentPreVo getRecentPre(@Valid SearchRecentPreRequest request);

    IPage<HosPrescription> getHosPrescriptionList(PrescriptionQueryRequest request);

    RecentPreVo getRecentPreById(Long id);

    Boolean withdrawPrescription(WithdrawPrescriptionRequest req);
}
