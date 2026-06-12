package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionInstructionMapper;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionInstruction;
import com.aihoo.api.doctor.app.service.PrescriptionInstructionService;
import org.springframework.stereotype.Service;

@Service("doctorApiPrescriptionInstructionServiceImpl")
public class PrescriptionInstructionServiceImpl
        extends ServiceImpl<HosPrescriptionInstructionMapper, HosPrescriptionInstruction> implements PrescriptionInstructionService {
}
