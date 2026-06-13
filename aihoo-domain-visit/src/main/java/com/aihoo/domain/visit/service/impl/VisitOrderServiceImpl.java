package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.model.entity.Visit;
import com.aihoo.domain.visit.model.mapper.VisitOrderMapper;
import com.aihoo.domain.visit.service.VisitOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VisitOrderServiceImpl extends ServiceImpl<VisitOrderMapper, Visit> implements VisitOrderService {
}
