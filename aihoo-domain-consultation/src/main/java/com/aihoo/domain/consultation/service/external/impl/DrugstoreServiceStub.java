package com.aihoo.domain.consultation.service.external.impl;

import com.aihoo.domain.consultation.model.external.Drugstore;
import com.aihoo.domain.consultation.service.external.DrugstoreService;
import org.springframework.stereotype.Service;

/**
 * 跨域 stub：药房服务空实现。迁移 hospital 域后删除整个 impl/。
 */
@Service
public class DrugstoreServiceStub implements DrugstoreService {

    @Override
    public Drugstore getById(String id) {
        return null;
    }
}