package com.aihoo.domain.prescription.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Classname HosPreDrugService
 * @Description hf
 * @Date 2020/9/24 15:02
 * @Created by ad
 */
public interface HosPreDrugService extends IService<HosPrescriptionDrug> {
    PageResult drugList(Map<String, Object> map);

    JSONObject getDrugDetails(Map<String, Object> map) throws Exception;

    Object getDrugDeliveryStatus(Map<String, Object> map) throws Exception;
}
