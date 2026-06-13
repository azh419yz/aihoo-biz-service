package com.aihoo.domain.patient.service.impl;

import com.aihoo.domain.patient.model.entity.PatientUserLog;
import com.aihoo.domain.patient.model.mapper.PatientUserLogMapper;
import com.aihoo.domain.patient.service.PatientUserLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PatientUserLogServiceImpl extends ServiceImpl<PatientUserLogMapper, PatientUserLog> implements PatientUserLogService {
}
