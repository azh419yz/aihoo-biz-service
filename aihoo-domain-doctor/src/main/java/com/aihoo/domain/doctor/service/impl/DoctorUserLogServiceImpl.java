package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.model.entity.DoctorUserLog;
import com.aihoo.domain.doctor.model.mapper.DoctorUserLogMapper;
import com.aihoo.domain.doctor.service.DoctorUserLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DoctorUserLogServiceImpl extends ServiceImpl<DoctorUserLogMapper, DoctorUserLog> implements DoctorUserLogService {
}
