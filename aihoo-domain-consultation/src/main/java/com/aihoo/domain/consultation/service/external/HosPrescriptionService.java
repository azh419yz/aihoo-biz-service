package com.aihoo.domain.consultation.service.external;

import com.aihoo.domain.consultation.model.external.HosPrescription;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 处方域服务占位接口。
 * TODO 跨域stub：迁移 prescription 域后改为引用 com.aihoo.domain.prescription.service.HosPrescriptionService
 */
public interface HosPrescriptionService {

    default List<HosPrescription> listByIds(Collection<String> ids) {
        return Collections.emptyList();
    }

    default HosPrescription getById(String id) {
        return null;
    }
}