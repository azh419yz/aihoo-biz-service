package com.aihoo.api.doctor.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.vo.HosSickVo;
import com.aihoo.domain.visit.model.entity.HosSick;

public interface HosSickService extends IService<HosSick> {

    HosSickVo findHosSickViewById(Long sickId);
}
