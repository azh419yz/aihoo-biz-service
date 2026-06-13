package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescriptionDisease;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDiseaseMapper;
import com.aihoo.domain.prescription.service.PrescriptionDiseaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("prescriptionDiseaseServiceImpl")
public class PrescriptionDiseaseServiceImpl extends ServiceImpl<HosPrescriptionDiseaseMapper, HosPrescriptionDisease>
        implements PrescriptionDiseaseService {
}
