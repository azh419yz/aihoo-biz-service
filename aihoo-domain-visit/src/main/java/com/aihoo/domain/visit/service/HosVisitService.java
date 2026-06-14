package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.dto.HosOrder;
import com.aihoo.domain.visit.dto.HosVisitBaseInfoVo;
import com.aihoo.domain.visit.dto.HosVisitHealthInfoVo;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface HosVisitService extends IService<HosVisit> {
    List visitOrderList(Map<String, String> map);
    HosOrder visitData(String id);
    String haveVisit(Map<String, String> map);
    String writeVisitResult(Map<String, String> map);
    Map visitResult(Map<String, String> map);
    Map<String, String> visitOrderCount(Map<String, String> map);
    Long countByDoctorUserId(String doctorUserId);
    HosVisitHealthInfoVo getHealthInfo(String hosVisitId);
    HosVisitBaseInfoVo getBaseInfo(String hosVisitId);
    JSONArray patientList(Map<String, Object> map);
}
