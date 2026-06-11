package com.aihoo.domain.doctor.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface DoctorUserService extends IService<DoctorUser> {

    List<DoctorUser> findDoctorUserAll();

    PageResult<DoctorUser> list(Map<String, Object> map);
}
