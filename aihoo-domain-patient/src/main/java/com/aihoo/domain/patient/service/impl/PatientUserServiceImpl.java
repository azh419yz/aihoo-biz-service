package com.aihoo.domain.patient.service.impl;

import com.aihoo.domain.patient.model.entity.PatientUser;
import com.aihoo.domain.patient.model.mapper.PatientUserMapper;
import com.aihoo.domain.patient.service.PatientUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PatientUserServiceImpl extends ServiceImpl<PatientUserMapper, PatientUser> implements PatientUserService {
}
