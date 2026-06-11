package com.aihoo.domain.prescription.model.mapper;

import com.aihoo.domain.prescription.model.entity.HosPreDrugOrder;
import com.aihoo.domain.prescription.model.vo.HosPreDrugVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @Classname HosPreDrugMapper
 * @Description hf
 * @Date 2020/9/24 15:10
 * @Created by ad
 */
public interface HosPreDrugMapper extends BaseMapper<HosPreDrugOrder> {

    List<HosPreDrugVo> drugList(Map<String, Object> map);

    HosPreDrugOrder getDrugDetails(String id);

    int getCount(Map<String, Object> map);
}
