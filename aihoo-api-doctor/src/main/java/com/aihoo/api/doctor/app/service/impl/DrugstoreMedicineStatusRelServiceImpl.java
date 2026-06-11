package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.domain.hospital.model.mapper.DrugstoreMedicineStatusRelMapper;
import com.aihoo.domain.hospital.model.entity.DrugstoreMedicineStatusRel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.service.DrugstoreMedicineStatusRelService;
import org.springframework.stereotype.Service;

@Service
public class DrugstoreMedicineStatusRelServiceImpl extends ServiceImpl<DrugstoreMedicineStatusRelMapper, DrugstoreMedicineStatusRel>
        implements DrugstoreMedicineStatusRelService {
}
