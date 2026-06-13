package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.model.mapper.DrugstoreMedicineStatusRelMapper;
import com.aihoo.domain.hospital.model.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.hospital.service.DrugstoreMedicineStatusRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("drugstoreMedicineStatusRelServiceImpl")
public class DrugstoreMedicineStatusRelServiceImpl extends ServiceImpl<DrugstoreMedicineStatusRelMapper, DrugstoreMedicineStatusRel>
        implements DrugstoreMedicineStatusRelService {
}
