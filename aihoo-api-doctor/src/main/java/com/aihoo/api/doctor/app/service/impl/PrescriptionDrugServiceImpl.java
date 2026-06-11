package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.mapper.HosPrescriptionDrugMapper;
import com.aihoo.api.doctor.app.model.HosPrescriptionDrug;
import com.aihoo.api.doctor.app.service.PrescriptionDrugService;
import org.springframework.stereotype.Service;

/**
 * @program: aihoo-root
 * @description:
 * @author: Mr.Li
 * @create: 2020-10-08 10:57
 **/
@Service
public class PrescriptionDrugServiceImpl extends ServiceImpl<HosPrescriptionDrugMapper, HosPrescriptionDrug> implements PrescriptionDrugService {
}
