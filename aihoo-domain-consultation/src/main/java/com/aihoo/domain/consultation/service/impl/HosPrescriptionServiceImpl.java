package com.aihoo.domain.consultation.service.impl;

import com.aihoo.domain.consultation.model.entity.HosPrescription;
import com.aihoo.domain.consultation.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.consultation.service.HosPrescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HosPrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription> implements HosPrescriptionService {
    @Override
    public Object getPrescriptionInnerVo(String id) { return null; }
}
