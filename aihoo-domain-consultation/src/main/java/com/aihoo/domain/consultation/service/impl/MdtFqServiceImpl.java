package com.aihoo.domain.consultation.service.impl;

import com.aihoo.domain.consultation.model.entity.Mdt;
import com.aihoo.domain.consultation.model.mapper.MdtMapper;
import com.aihoo.domain.consultation.service.MdtFqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MdtFqServiceImpl extends ServiceImpl<MdtMapper, Mdt> implements MdtFqService {
    @Override
    public List<Map<String, Object>> fuzzQueryMdt(Map<String, Object> map) {
        return Collections.emptyList();
    }
}
