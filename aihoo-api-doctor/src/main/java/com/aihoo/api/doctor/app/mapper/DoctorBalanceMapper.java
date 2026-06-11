package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.DoctorBalance;

public interface DoctorBalanceMapper extends BaseMapper<DoctorBalance> {
    /**
     * 通过医生id查询数据
     * @param doctorId
     * @return
     */
    DoctorBalance selectByDoctorId(String doctorId);
}