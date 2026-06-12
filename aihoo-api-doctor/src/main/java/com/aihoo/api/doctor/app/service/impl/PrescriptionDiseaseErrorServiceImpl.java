package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDiseaseErrorMapper;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDiseaseError;
import com.aihoo.api.doctor.app.service.PrescriptionDiseaseErrorService;
import org.springframework.stereotype.Service;

/**
 * @program: aihoo-root
 * @description:
 * @author: Mr.Li
 * @create: 2020-10-08 09:39
 **/
@Service
public class PrescriptionDiseaseErrorServiceImpl extends ServiceImpl<HosPrescriptionDiseaseErrorMapper, HosPrescriptionDiseaseError> implements PrescriptionDiseaseErrorService {
}
