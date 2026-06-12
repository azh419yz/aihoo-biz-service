package com.aihoo.api.doctor.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.vo.HosSickVo;
import com.aihoo.domain.patient.model.entity.PatientUser;

import java.util.List;
import java.util.Map;

public interface PatientService extends IService<PatientUser> {
    /**
     * 患者列表
     *
     * @return
     */
    List<Map> patientList(Map<String, String> map);

    /**
     * 患者详情
     *
     * @param map
     * @return
     */
    Map patientMsg(Map<String, String> map);

    List<HosSickVo> patientList(String sickName);

    HosSickVo patientMsg(String id);
}
