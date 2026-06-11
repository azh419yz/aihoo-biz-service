package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.HosRevisit;

import java.util.Date;
import java.util.List;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/25 15:46
 */
public interface HosRevisitMapper extends BaseMapper<HosRevisit> {

    List<HosRevisit> selectDoctorIdAndStartTime(String doctorId, Date revisitStartTime);
}