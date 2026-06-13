package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.DepartmentService;
import com.aihoo.domain.hospital.model.entity.Department;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.DepartmentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
}
