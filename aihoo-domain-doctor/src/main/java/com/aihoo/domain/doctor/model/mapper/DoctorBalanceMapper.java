package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorBalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DoctorBalanceMapper extends BaseMapper<DoctorBalance> {
    DoctorBalance selectByDoctorId(@Param("doctorId") String doctorId);
}
