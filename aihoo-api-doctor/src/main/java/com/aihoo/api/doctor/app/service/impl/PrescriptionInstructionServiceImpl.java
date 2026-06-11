package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.mapper.HosPrescriptionInstructionMapper;
import com.aihoo.api.doctor.app.model.HosPrescriptionInstruction;
import com.aihoo.api.doctor.app.service.PrescriptionInstructionService;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionInstructionServiceImpl
        extends ServiceImpl<HosPrescriptionInstructionMapper, HosPrescriptionInstruction> implements PrescriptionInstructionService {
}
