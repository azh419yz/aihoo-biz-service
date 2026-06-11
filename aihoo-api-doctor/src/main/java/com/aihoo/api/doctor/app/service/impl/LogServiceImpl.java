package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.api.doctor.app.mapper.HosPrescriptionLogMapper;
import com.aihoo.api.doctor.app.mapper.HosRevisitLogMapper;
import com.aihoo.api.doctor.app.mapper.HosSickMapper;
import com.aihoo.api.doctor.app.mapper.HosVisitLogMapper;
import com.aihoo.api.doctor.app.model.*;
import com.aihoo.api.doctor.app.service.LogService;
import com.aihoo.api.doctor.common.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/25 14:10
 * @description：日志
 * @modified By：
 * @version: 2020/9/25 14点10分$
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private HosVisitLogMapper hosVisitLogMapper;
    @Autowired
    private HosRevisitLogMapper hosRevisitLogMapper;
    @Autowired
    private HosPrescriptionLogMapper hosPrescriptionLogMapper;
    @Autowired
    private HosSickMapper hosSickMapper;

    //  `hos_revisit_id` bigint(32) NOT NULL COMMENT '在线问诊信息id',
    //  `type` varchar(10) NOT NULL DEFAULT 'PATIENT' COMMENT '操作人类型 PATIENT-患者 DOCKER-医生',
    //  `patient_user_id` bigint(32) DEFAULT NULL COMMENT '患者操作人id',
    //  `doctor_user_id` bigint(32) DEFAULT NULL COMMENT '医生操作人id',
    //  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'WAIT' COMMENT '状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单  START复诊进行中 END订单完成',
    //  `remark` bigint(32) NOT NULL COMMENT '日志',

    /**
     * 保存医生复诊日志
     *
     * @param hosRevisit 复诊订单
     * @param status     状态id
     * @param remark     日志
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setRevisitLog(HosRevisit hosRevisit, String status, String remark) {
        HosRevisitLog hosRevisitLog = new HosRevisitLog();
        hosRevisitLog.setHosRevisitId(hosRevisit.getId());
        hosRevisitLog.setType("DOCKER");
        hosRevisitLog.setDoctorUserId(getDoctorUserId());
        hosRevisitLog.setPatientUserId(hosRevisit.getPatientUserId());
        hosRevisitLog.setStatus(status);
        hosRevisitLog.setRemark(remark);
        hosRevisitLog.setSickId(hosRevisit.getHosSickId());
        int insert = hosRevisitLogMapper.insert(hosRevisitLog);
        if (insert == 1) return true;
        return false;
    }

    /**
     * 保存医生问诊日志
     *
     * @param hosVisit 问诊订单
     * @param status   状态id
     * @param remark   日志
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setVisitLog(HosVisit hosVisit, String status, String remark) {
        HosVisitLog hosVisitLog = new HosVisitLog();
        hosVisitLog.setHosVisitId(hosVisit.getId());
        hosVisitLog.setType("DOCKER");
        hosVisitLog.setDoctorUserId(getDoctorUserId());
        hosVisitLog.setPatientUserId(hosVisit.getPatientUserId());
        hosVisitLog.setStatus(status);
        hosVisitLog.setRemark(remark);
        hosVisitLog.setSickId(hosVisit.getHosSickId());
        int insert = hosVisitLogMapper.insert(hosVisitLog);
        if (insert == 1) return true;
        return false;
    }

    /**
     * 处方日志
     *
     * @param hosPrescription 处方
     * @param status          订单状态
     * @param remark          订单日志
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setPrescriptionLog(HosPrescription hosPrescription, String status, String remark) {
        HosPrescriptionLog hosPrescriptionLog = new HosPrescriptionLog();
        hosPrescriptionLog.setType("DOCKER");
        hosPrescriptionLog.setHosPrescriptionId(hosPrescription.getId());
        hosPrescriptionLog.setPatientUserId(hosPrescription.getPatientUserId());
        hosPrescriptionLog.setDoctorUserId(getDoctorUserId());
        hosPrescriptionLog.setStatus(status);
        hosPrescriptionLog.setRemark(remark);
        hosPrescriptionLog.setSickId(hosPrescription.getHosSickId());
        int insert = hosPrescriptionLogMapper.insert(hosPrescriptionLog);
        if (insert == 1) return true;
        return false;
    }

    public String getDoctorUserId() {
        return AuthUtil.getLoginUser().getId();
    }

    @Override
    public String getDoctorUserName() {
        return AuthUtil.getLoginUser().getName();
    }

    @Override
    public String getSickName(String sickId) {
        HosSick hosSick = hosSickMapper.selectById(sickId);
        return hosSick.getName();
    }
}
