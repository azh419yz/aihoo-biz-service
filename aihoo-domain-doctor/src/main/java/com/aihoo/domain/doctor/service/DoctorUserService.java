package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface DoctorUserService extends IService<DoctorUser> {
    DoctorUser findDoctorUserById(String id);
    List<DoctorUser> findDoctorUserAll();
    Object list(Map<String, Object> map);
}
