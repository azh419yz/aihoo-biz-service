package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescriptionFee;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionFeeMapper;
import com.aihoo.domain.prescription.service.PrescriptionFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("prescriptionFeeServiceImpl")
public class PrescriptionFeeServiceImpl extends ServiceImpl<HosPrescriptionFeeMapper, HosPrescriptionFee>
        implements PrescriptionFeeService {
}
