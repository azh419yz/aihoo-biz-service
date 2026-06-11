package com.aihoo.domain.consultation.service.external.impl;

import com.aihoo.domain.consultation.model.external.HosPrescription;
import com.aihoo.domain.consultation.service.external.HosPrescriptionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 跨域 stub：处方服务空实现。迁移 prescription 域后删除整个 impl/。
 */
@Service
public class HosPrescriptionServiceStub implements HosPrescriptionService {

    @Override
    public List<HosPrescription> listByIds(Collection<String> ids) {
        return Collections.emptyList();
    }

    @Override
    public HosPrescription getById(String id) {
        return null;
    }
}