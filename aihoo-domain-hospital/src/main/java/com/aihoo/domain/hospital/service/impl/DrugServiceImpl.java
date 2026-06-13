package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.DrugService;
import com.aihoo.domain.hospital.model.entity.Drug;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.DrugMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {
}
