package com.aihoo.domain.visit.service;

import com.aihoo.domain.patient.dto.HosSickVo;
import com.aihoo.domain.visit.model.entity.HosSick;
import com.baomidou.mybatisplus.extension.service.IService;

public interface HosSickService extends IService<HosSick> {
    HosSickVo findHosSickViewById(Long sickId);
}
