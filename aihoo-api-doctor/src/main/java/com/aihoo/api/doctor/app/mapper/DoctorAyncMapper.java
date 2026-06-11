package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.DoctorAync;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 20:36
 * @description：接诊设置表
 */
public interface DoctorAyncMapper extends BaseMapper<DoctorAync> {
    /**
     * 通过医生id查询
     * @param doctorId 医生id
     * @return 接诊信息实体
     */
    DoctorAync selectByDoctorId(String doctorId);
}