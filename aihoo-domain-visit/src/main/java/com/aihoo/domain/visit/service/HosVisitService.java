package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.model.entity.HosVisit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface HosVisitService extends IService<HosVisit> {
    List visitOrderList(Map<String, String> map);
    Object visitData(String id);
    String haveVisit(Map<String, String> map);
    String writeVisitResult(Map<String, String> map);
    Map visitResult(Map<String, String> map);
    Map<String, String> visitOrderCount(Map<String, String> map);
    Long countByDoctorUserId(String doctorUserId);
    Object getHealthInfo(String hosVisitId);
    Object getBaseInfo(String hosVisitId);
    Object patientList(Map<String, Object> map);
}
