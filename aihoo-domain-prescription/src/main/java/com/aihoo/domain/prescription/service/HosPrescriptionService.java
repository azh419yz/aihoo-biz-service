package com.aihoo.domain.prescription.service;

import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.vo.HosPrescriptionInnerVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Classname HosPrescriptionService
 * @Description hf
 * @Date 2020/9/24 10:00
 * @Created by ad
 */
public interface HosPrescriptionService extends IService<HosPrescription> {
    HosPrescriptionInnerVo getPrescriptionInnerVo(String id);
}
