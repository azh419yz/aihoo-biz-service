package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrugError;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugErrorMapper;
import com.aihoo.domain.prescription.service.PrescriptionDrugErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("prescriptionDrugErrorServiceImpl")
public class PrescriptionDrugErrorServiceImpl extends ServiceImpl<HosPrescriptionDrugErrorMapper, HosPrescriptionDrugError>
        implements PrescriptionDrugErrorService {
}
