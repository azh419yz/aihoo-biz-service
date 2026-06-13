package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescriptionDiseaseError;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDiseaseErrorMapper;
import com.aihoo.domain.prescription.service.PrescriptionDiseaseErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("prescriptionDiseaseErrorServiceImpl")
public class PrescriptionDiseaseErrorServiceImpl extends ServiceImpl<HosPrescriptionDiseaseErrorMapper, HosPrescriptionDiseaseError>
        implements PrescriptionDiseaseErrorService {
}
