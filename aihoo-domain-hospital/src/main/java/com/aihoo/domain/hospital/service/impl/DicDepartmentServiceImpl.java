package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.DicDepartmentService;
import com.aihoo.domain.hospital.model.entity.DicDepartment;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.DicDepartmentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class DicDepartmentServiceImpl extends ServiceImpl<DicDepartmentMapper, DicDepartment> implements DicDepartmentService {
}
