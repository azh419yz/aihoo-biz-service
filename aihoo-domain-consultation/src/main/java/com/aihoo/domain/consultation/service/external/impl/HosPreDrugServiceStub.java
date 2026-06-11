package com.aihoo.domain.consultation.service.external.impl;

import com.aihoo.domain.consultation.model.external.HosPrescriptionDrug;
import com.aihoo.domain.consultation.service.external.HosPreDrugService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 跨域 stub：处方药品服务空实现。迁移 prescription 域后删除整个 impl/。
 */
@Service
public class HosPreDrugServiceStub implements HosPreDrugService {

    @Override
    public <T> List<HosPrescriptionDrug> list(Wrapper<T> wrapper) {
        return Collections.emptyList();
    }
}