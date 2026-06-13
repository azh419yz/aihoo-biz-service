package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.DrugCountService;
import com.aihoo.domain.hospital.model.entity.DrugCount;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.DrugCountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class DrugCountServiceImpl extends ServiceImpl<DrugCountMapper, DrugCount> implements DrugCountService {
}
