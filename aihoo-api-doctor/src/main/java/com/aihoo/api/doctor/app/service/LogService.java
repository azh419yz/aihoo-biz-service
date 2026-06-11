package com.aihoo.api.doctor.app.service;

import com.aihoo.api.doctor.app.model.HosPrescription;
import com.aihoo.api.doctor.app.model.HosRevisit;
import com.aihoo.api.doctor.app.model.HosVisit;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/25 14:10
 * @description：日志
 */
public interface LogService {
    /**
     * 保存医生复诊日志
     *
     * @param hosRevisit
     * @param status
     * @param remark
     * @return
     */
    boolean setRevisitLog(HosRevisit hosRevisit, String status, String remark);

    /**
     * 保存患者问诊日志
     *
     * @param hosVisit
     * @param status
     * @param remark
     * @return
     */
    boolean setVisitLog(HosVisit hosVisit, String status, String remark);

    /**
     * 处方日志
     *
     * @param hosPrescription 处方
     * @param status          订单状态
     * @param remark          订单日志
     * @return
     */
    boolean setPrescriptionLog(HosPrescription hosPrescription, String status, String remark);

    String getDoctorUserName();

    String getSickName(String sickId);
}
