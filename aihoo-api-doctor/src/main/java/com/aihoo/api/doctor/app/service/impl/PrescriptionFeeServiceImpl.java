package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.mapper.HosPrescriptionFeeMapper;
import com.aihoo.api.doctor.app.model.HosPrescriptionFee;
import com.aihoo.api.doctor.app.service.PrescriptionFeeService;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionFeeServiceImpl
        extends ServiceImpl<HosPrescriptionFeeMapper, HosPrescriptionFee> implements PrescriptionFeeService {
}
