package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionDrugErrorMapper;
import com.aihoo.domain.prescription.model.entity.HosPrescriptionDrugError;
import com.aihoo.api.doctor.app.service.PrescriptionDrugErrorService;
import org.springframework.stereotype.Service;

/**
 * @program: aihoo-root
 * @description:
 * @author: Mr.Li
 * @create: 2020-10-08 10:57
 **/
@Service
public class PrescriptionDrugErrorServiceImpl extends ServiceImpl<HosPrescriptionDrugErrorMapper, HosPrescriptionDrugError> implements PrescriptionDrugErrorService {
}
