package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.model.entity.DoctorBalance;
import com.aihoo.domain.doctor.model.mapper.DoctorBalanceMapper;
import com.aihoo.domain.doctor.service.DoctorBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DoctorBalanceServiceImpl extends ServiceImpl<DoctorBalanceMapper, DoctorBalance> implements DoctorBalanceService {
}
