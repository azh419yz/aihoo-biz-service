package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.DoctorSetTimes;

import java.util.List;
import java.util.Map;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 21:10
 */
public interface DoctorSetTimesMapper extends BaseMapper<DoctorSetTimes> {

    List<Map> selectListByTypeAndDoctorId(String doctorId, String type);
}