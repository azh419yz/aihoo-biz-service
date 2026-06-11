package com.aihoo.domain.hospital.model.mapper;

import com.aihoo.domain.hospital.model.entity.Hospital;
import com.aihoo.domain.hospital.model.vo.HospitalIbfkVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Classname HospitalMapper
 * @Description hf
 * @Date 2020/9/17 9:19
 * @Created by ad
 */
public interface HospitalMapper extends BaseMapper<Hospital> {
    /**
     * 查询 医院的 id和名称
     * @return
     */
    List<HospitalIbfkVo> selectHospital();
}
