package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescriptionInstruction;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionInstructionMapper;
import com.aihoo.domain.prescription.service.PrescriptionInstructionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionInstructionServiceImpl
        extends ServiceImpl<HosPrescriptionInstructionMapper, HosPrescriptionInstruction> implements PrescriptionInstructionService {
}
