package com.aihoo.domain.visit.service;

import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.entity.HosVisit;

/**
 * 跨实体日志服务（复诊/问诊/处方）。归属 visit 域（H rule 允许调用 prescription 域 mapper）。
 */
public interface LogService {
    /**
     * 保存医生复诊日志
     */
    boolean setRevisitLog(HosRevisit hosRevisit, String status, String remark);

    /**
     * 保存患者问诊日志
     */
    boolean setVisitLog(HosVisit hosVisit, String status, String remark);

    /**
     * 处方日志
     */
    boolean setPrescriptionLog(HosPrescription hosPrescription, String status, String remark);

    String getDoctorUserName();

    String getSickName(String sickId);
}
