package com.aihoo.domain.doctor.service;

import com.aihoo.domain.doctor.model.entity.DoctorDirectory;
import com.aihoo.domain.doctor.dto.DoctorDirectoryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DoctorDirectoryService extends IService<DoctorDirectory> {
    List<DoctorDirectoryVo> findDoctorDirectoryList(String sickName);
}
