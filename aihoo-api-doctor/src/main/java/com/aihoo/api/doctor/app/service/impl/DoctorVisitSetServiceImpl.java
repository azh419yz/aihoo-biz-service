package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.doctor.model.mapper.DoctorVisitSetMapper;
import com.aihoo.api.doctor.app.model.DoctorVisitSet;
import com.aihoo.api.doctor.app.service.DoctorVisitSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问诊设置表 服务实现类
 * </p>
 *
 */
@Service("doctorApiDoctorVisitSetServiceImpl")
@RequiredArgsConstructor
public class DoctorVisitSetServiceImpl extends ServiceImpl<DoctorVisitSetMapper, DoctorVisitSet> implements DoctorVisitSetService {


}
