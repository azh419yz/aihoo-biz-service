package com.aihoo.domain.doctor.model.mapper;

import com.aihoo.domain.doctor.model.entity.DoctorAync;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname DoctorAyncMapper
 * @Description hf
 * @Date 2020/9/28 13:57
 * @Created by ad
 */
public interface DoctorAyncMapper extends BaseMapper<DoctorAync> {

    List<DoctorAync> findDoctorAyncByUserIds(@Param("doctorUserIds") List<String> doctorUserIds);
}
