package com.aihoo.domain.patient.service;

import com.aihoo.domain.patient.dto.HosSickVo;
import com.aihoo.domain.patient.model.entity.PatientUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PatientService extends IService<PatientUser> {
    List<Map> patientList(Map<String, String> map);

    Map patientMsg(Map<String, String> map);

    List<HosSickVo> patientList(String sickName);

    HosSickVo patientMsg(String id);
}
