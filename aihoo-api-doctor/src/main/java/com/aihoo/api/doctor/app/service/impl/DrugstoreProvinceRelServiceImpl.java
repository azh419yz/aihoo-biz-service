package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.domain.hospital.model.mapper.DrugstoreProvinceRelMapper;
import com.aihoo.domain.hospital.model.entity.DrugstoreProvinceRel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.service.DrugstoreProvinceRelService;
import org.springframework.stereotype.Service;

@Service("doctorApiDrugstoreProvinceRelServiceImpl")
public class DrugstoreProvinceRelServiceImpl extends ServiceImpl<DrugstoreProvinceRelMapper, DrugstoreProvinceRel>
        implements DrugstoreProvinceRelService {
}
