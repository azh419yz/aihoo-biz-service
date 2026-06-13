package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.model.entity.Visit;
import com.aihoo.domain.visit.model.mapper.RevisitOrderMapper;
import com.aihoo.domain.visit.service.RevisitOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RevisitOrderServiceImpl extends ServiceImpl<RevisitOrderMapper, Visit> implements RevisitOrderService {
}
