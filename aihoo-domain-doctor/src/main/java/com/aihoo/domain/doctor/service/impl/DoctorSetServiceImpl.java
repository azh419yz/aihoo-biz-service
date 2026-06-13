package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.model.entity.DoctorSet;
import com.aihoo.domain.doctor.model.mapper.DoctorSetMapper;
import com.aihoo.domain.doctor.service.DoctorSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DoctorSetServiceImpl extends ServiceImpl<DoctorSetMapper, DoctorSet> implements DoctorSetService {
}
