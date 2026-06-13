package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrug;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugMapper;
import com.aihoo.domain.prescription.service.PrescriptionDrugService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("prescriptionDrugServiceImpl")
public class PrescriptionDrugServiceImpl extends ServiceImpl<HosPrescriptionDrugMapper, HosPrescriptionDrug>
        implements PrescriptionDrugService {
}
