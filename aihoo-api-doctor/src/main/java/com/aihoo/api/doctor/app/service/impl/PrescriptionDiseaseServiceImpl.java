package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.mapper.HosPrescriptionDiseaseMapper;
import com.aihoo.api.doctor.app.model.HosPrescriptionDisease;
import com.aihoo.api.doctor.app.service.PrescriptionDiseaseService;
import org.springframework.stereotype.Service;

/**
 * @program: aihoo-root
 * @description:
 * @author: Mr.Li
 * @create: 2020-10-08 09:39
 **/
@Service
public class PrescriptionDiseaseServiceImpl extends ServiceImpl<HosPrescriptionDiseaseMapper, HosPrescriptionDisease> implements PrescriptionDiseaseService {
}
