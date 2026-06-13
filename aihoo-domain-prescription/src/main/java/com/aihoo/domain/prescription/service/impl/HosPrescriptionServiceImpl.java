package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescription;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.domain.prescription.service.HosPrescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("hosPrescriptionServiceImpl")
public class HosPrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription>
        implements HosPrescriptionService {
}
