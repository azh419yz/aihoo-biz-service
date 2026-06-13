package com.aihoo.domain.doctor.service.impl;

import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, DoctorUser> implements DoctorUserService {
    @Override
    public DoctorUser findDoctorUserById(String id) { return baseMapper.selectById(id); }
    @Override
    public List<DoctorUser> findDoctorUserAll() { return baseMapper.selectList(null); }
    @Override
    public Object list(Map<String, Object> map) { return null; }
}
