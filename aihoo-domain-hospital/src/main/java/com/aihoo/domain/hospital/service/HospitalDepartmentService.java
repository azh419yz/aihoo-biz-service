package com.aihoo.domain.hospital.service;

import com.aihoo.domain.hospital.model.entity.HospitalDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Classname HospitalDepartmentService
 * @Description hf
 * @Date 2020/9/18 17:02
 * @Created by ad
 */
public interface HospitalDepartmentService extends IService<HospitalDepartment> {
    List<HospitalDepartment> findDepartCodeAllByHospitalId(String id);

    void deleteByDepartCodes(List<String> newIds,String hospitalId);
}
