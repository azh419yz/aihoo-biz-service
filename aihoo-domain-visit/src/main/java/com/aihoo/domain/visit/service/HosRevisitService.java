package com.aihoo.domain.visit.service;

import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface HosRevisitService extends IService<HosRevisit> {
    String haveRevisit(Map<String, String> map);
    List revisitOrderList(Map<String, String> map);
    Object revisitData(Map<String, Object> map);
    List diseaseList(Map<String, Object> map);
    List drugList(Map<String, String> map);
    List<Map<String, String>> dictList(String type);
    String sign(Map<String, Object> map);
    String setPrescription(Map<String, Object> map);
    Map getPrescriptionStatus(Map<String, Object> map);
    Map prescription(Map<String, Object> map);
    String writeRevisitResult(Map<String, String> map);
    Map revisitResult(Map<String, String> map);
    boolean commitPrescription(String id);
    Drug getOneDrug(Map<String, String> map);
    Map<String, Object> getPrescription(Map<String, String> map);
    Object getPrescriptionList(Map<String, String> map);
    Object getArchives(String id);
    Map<String, String> revisitOrderCount(Map<String, String> map);
}
