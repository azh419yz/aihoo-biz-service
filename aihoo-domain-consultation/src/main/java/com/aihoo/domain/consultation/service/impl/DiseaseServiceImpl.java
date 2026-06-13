package com.aihoo.domain.consultation.service.impl;

import com.aihoo.domain.consultation.model.entity.Disease;
import com.aihoo.domain.consultation.model.mapper.DiseaseMapper;
import com.aihoo.domain.consultation.service.DiseaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements DiseaseService {
}
