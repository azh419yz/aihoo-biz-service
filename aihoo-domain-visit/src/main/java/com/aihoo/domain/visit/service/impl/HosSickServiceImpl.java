package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.model.entity.HosSick;
import com.aihoo.domain.visit.model.mapper.HosSickMapper;
import com.aihoo.domain.visit.service.HosSickService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public abstract class HosSickServiceImpl extends ServiceImpl<HosSickMapper, HosSick> implements HosSickService {
}
