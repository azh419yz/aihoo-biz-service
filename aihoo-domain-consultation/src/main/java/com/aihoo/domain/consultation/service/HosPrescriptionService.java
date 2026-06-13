package com.aihoo.domain.consultation.service;

import com.aihoo.domain.consultation.model.entity.HosPrescription;
import com.baomidou.mybatisplus.extension.service.IService;

public interface HosPrescriptionService extends IService<HosPrescription> {
    Object getPrescriptionInnerVo(String id);
}
