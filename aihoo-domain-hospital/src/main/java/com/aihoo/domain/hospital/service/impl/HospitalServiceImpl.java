package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.HospitalService;
import com.aihoo.domain.hospital.model.entity.Hospital;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.HospitalMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {
}
