package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.model.mapper.DrugstoreProvinceRelMapper;
import com.aihoo.domain.hospital.model.entity.DrugstoreProvinceRel;
import com.aihoo.domain.hospital.service.DrugstoreProvinceRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("drugstoreProvinceRelServiceImpl")
public class DrugstoreProvinceRelServiceImpl extends ServiceImpl<DrugstoreProvinceRelMapper, DrugstoreProvinceRel>
        implements DrugstoreProvinceRelService {
}
