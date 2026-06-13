package com.aihoo.domain.prescription.service;

import com.aihoo.common.JsonResult;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.prescription.dto.PrescriptionQueryRequest;
import com.aihoo.domain.prescription.dto.RecentPreVo;
import com.aihoo.domain.prescription.dto.SavePrescriptionRequest;
import com.aihoo.domain.prescription.dto.SearchRecentPreRequest;
import com.aihoo.domain.prescription.dto.WithdrawPrescriptionRequest;
import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.sys.model.entity.Disease;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
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
