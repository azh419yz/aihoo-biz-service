package com.aihoo.domain.visit.service.impl;

import com.aihoo.domain.visit.model.entity.HosSickHealthRecords;
import com.aihoo.domain.visit.model.mapper.HosSickHealthRecordsMapper;
import com.aihoo.domain.visit.service.HosSickHealthRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("hosSickHealthRecordsServiceImpl")
public class HosSickHealthRecordsServiceImpl extends ServiceImpl<HosSickHealthRecordsMapper, HosSickHealthRecords>
        implements HosSickHealthRecordsService {
}
