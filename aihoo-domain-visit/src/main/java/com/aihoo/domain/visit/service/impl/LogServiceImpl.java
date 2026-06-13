package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionLog;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionLogMapper;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.entity.HosRevisitLog;
import com.aihoo.domain.visit.model.entity.HosSick;
import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.entity.HosVisitLog;
import com.aihoo.domain.visit.model.mapper.HosRevisitLogMapper;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.domain.visit.model.mapper.HosVisitLogMapper;
import com.aihoo.domain.visit.service.LogService;
import com.aihoo.security.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("visitLogServiceImpl")
public class LogServiceImpl implements LogService {
    @Autowired
    private HosVisitLogMapper hosVisitLogMapper;
    @Autowired
    private HosRevisitLogMapper hosRevisitLogMapper;
    @Autowired
    private HosPrescriptionLogMapper hosPrescriptionLogMapper;
    @Autowired
    private HosSickMapper hosSickMapper;

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
        return AuthUtil.getLoginUserId();
    }

    @Override
    public String getDoctorUserName() {
        return AuthUtil.getLoginUserName();
    }

    @Override
    public String getSickName(String sickId) {
        HosSick hosSick = hosSickMapper.selectById(sickId);
        return hosSick.getName();
    }
}
