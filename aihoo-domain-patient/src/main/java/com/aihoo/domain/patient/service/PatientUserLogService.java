package com.aihoo.domain.patient.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.entity.PatientUserLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 患者用户日志服务。
 *
 * @author ad
 * @since 2020/10/20 15:09
 */
public interface PatientUserLogService extends IService<PatientUserLog> {

    public boolean saveUserLog( List<PatientUser> patientUsers , HttpServletRequest request, String mobile);
}
