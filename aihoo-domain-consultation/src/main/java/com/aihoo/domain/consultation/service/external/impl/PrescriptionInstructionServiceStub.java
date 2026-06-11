package com.aihoo.domain.consultation.service.external.impl;

import com.aihoo.domain.consultation.model.external.HosPrescriptionInstruction;
import com.aihoo.domain.consultation.service.external.PrescriptionInstructionService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.stereotype.Service;

/**
 * 跨域 stub：处方用法用量服务空实现。迁移 prescription 域后删除整个 impl/。
 */
@Service
public class PrescriptionInstructionServiceStub implements PrescriptionInstructionService {

    @Override
    public <T> HosPrescriptionInstruction getOne(Wrapper<T> wrapper) {
        return null;
    }
}