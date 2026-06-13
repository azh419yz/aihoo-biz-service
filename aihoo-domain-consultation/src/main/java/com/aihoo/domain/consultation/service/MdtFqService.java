package com.aihoo.domain.consultation.service;

import com.aihoo.domain.consultation.model.entity.Mdt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface MdtFqService extends IService<Mdt> {
    List<Map<String, Object>> fuzzQueryMdt(Map<String, Object> map);
}
