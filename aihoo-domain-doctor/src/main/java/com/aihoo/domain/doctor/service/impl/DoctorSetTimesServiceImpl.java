package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.service.DoctorSetTimesService;
import com.aihoo.domain.doctor.model.entity.DoctorSetTimes;
import org.springframework.stereotype.Service;
import com.aihoo.domain.doctor.model.mapper.DoctorSetTimesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class DoctorSetTimesServiceImpl extends ServiceImpl<DoctorSetTimesMapper, DoctorSetTimes> implements DoctorSetTimesService {
}
