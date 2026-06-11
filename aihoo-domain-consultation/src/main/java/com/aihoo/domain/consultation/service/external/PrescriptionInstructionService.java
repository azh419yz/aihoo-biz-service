package com.aihoo.domain.consultation.service.external;

import com.aihoo.domain.consultation.model.external.HosPrescriptionInstruction;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * 处方用法用量服务占位接口。
 * TODO 跨域stub：迁移 prescription 域后改为引用 com.aihoo.domain.prescription.service.PrescriptionInstructionService
 */
public interface PrescriptionInstructionService {

    default <T> HosPrescriptionInstruction getOne(Wrapper<T> wrapper) {
        return null;
    }
}