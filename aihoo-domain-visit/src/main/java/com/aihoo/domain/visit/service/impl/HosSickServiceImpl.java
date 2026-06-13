package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.model.entity.Visit;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.domain.visit.service.HosSickService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HosSickServiceImpl extends ServiceImpl<HosSickMapper, Visit> implements HosSickService {
}
