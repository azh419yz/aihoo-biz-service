package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.model.entity.HospitalDepartment;
import com.aihoo.domain.hospital.model.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.hospital.service.HospitalDepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname HospitalDepartmentServiceImpl
 * @Description hf
 * @Date 2020/9/18 17:04
 * @Created by ad
 */
@Service
public class HospitalDepartmentServiceImpl extends ServiceImpl<HospitalDepartmentMapper, HospitalDepartment> implements HospitalDepartmentService {

    @Resource
    private HospitalDepartmentMapper hospitalDepartmentMapper;

    @Override
    public List<HospitalDepartment> findDepartCodeAllByHospitalId(String id) {
        return this.hospitalDepartmentMapper.
                selectList(new QueryWrapper<HospitalDepartment>().eq("hospital_id", id));
    }

    @Override
    public void deleteByDepartCodes(List<String> newIds,String hospitalId) {
        this.hospitalDepartmentMapper.deleteByDepartCodes(newIds,hospitalId);
    }

}
