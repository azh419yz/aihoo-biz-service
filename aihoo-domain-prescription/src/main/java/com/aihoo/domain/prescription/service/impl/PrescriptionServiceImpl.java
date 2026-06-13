package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.service.PrescriptionService;
import com.aihoo.domain.prescription.model.entity.Prescription;
import org.springframework.stereotype.Service;
import com.aihoo.domain.prescription.model.mapper.PrescriptionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class PrescriptionServiceImpl extends ServiceImpl<PrescriptionMapper, Prescription> implements PrescriptionService {
}
