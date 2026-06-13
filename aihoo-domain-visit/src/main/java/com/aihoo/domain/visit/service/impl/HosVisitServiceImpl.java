package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.model.entity.HosVisit;
import com.aihoo.domain.visit.model.mapper.HosVisitMapper;
import com.aihoo.domain.visit.service.HosVisitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public abstract class HosVisitServiceImpl extends ServiceImpl<HosVisitMapper, HosVisit> implements HosVisitService {
}
