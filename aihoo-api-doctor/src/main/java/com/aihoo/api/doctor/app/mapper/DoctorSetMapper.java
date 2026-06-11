package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.DoctorSet;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 21:09
 * @description：接诊设置表
 */
public interface DoctorSetMapper extends BaseMapper<DoctorSet> {
    /**
     * 医生设置表
     *
     * @param id
     * @return
     */
    DoctorSet selectDoctorUserId(String id);
}