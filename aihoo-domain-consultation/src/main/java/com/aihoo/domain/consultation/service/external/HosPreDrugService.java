package com.aihoo.domain.consultation.service.external;

import com.aihoo.domain.consultation.model.external.HosPrescriptionDrug;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.util.Collections;
import java.util.List;

/**
 * 处方药品服务占位接口。
 * TODO 跨域stub：迁移 prescription 域后改为引用 com.aihoo.domain.prescription.service.HosPreDrugService
 */
public interface HosPreDrugService {

    default <T> List<HosPrescriptionDrug> list(Wrapper<T> wrapper) {
        return Collections.emptyList();
    }
}