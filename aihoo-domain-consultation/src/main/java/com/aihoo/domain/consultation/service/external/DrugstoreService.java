package com.aihoo.domain.consultation.service.external;

import com.aihoo.domain.consultation.model.external.Drugstore;

/**
 * 药房服务占位接口。
 * TODO 跨域stub：迁移 hospital 域后改为引用 com.aihoo.domain.hospital.service.DrugstoreService
 *        注意：hospital 域的 DrugstoreService 使用的是 hospital 域的 Drugstore 实体，迁移后需调整这里 stub 类型的引用。
 */
public interface DrugstoreService {

    default Drugstore getById(String id) {
        return null;
    }
}