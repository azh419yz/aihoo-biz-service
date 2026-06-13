package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.DrugstoreMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class DrugstoreServiceImpl extends ServiceImpl<DrugstoreMapper, Drugstore> implements DrugstoreService {
}
