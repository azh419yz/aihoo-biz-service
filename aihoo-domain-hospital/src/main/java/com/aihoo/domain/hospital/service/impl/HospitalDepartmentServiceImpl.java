package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.model.entity.HospitalDepartment;
import com.aihoo.domain.hospital.model.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.hospital.service.HospitalDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HospitalDepartmentServiceImpl extends ServiceImpl<HospitalDepartmentMapper, HospitalDepartment> implements HospitalDepartmentService {
}
