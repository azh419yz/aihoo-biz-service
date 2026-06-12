package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.doctor.model.mapper.DoctorWelcomeMessageSetMapper;
import com.aihoo.domain.doctor.model.entity.DoctorWelcomeMessageSet;
import com.aihoo.api.doctor.app.service.DoctorWelcomeMessageSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 欢迎语设置表 服务实现类
 * </p>
 *
 */
@Service("doctorApiDoctorWelcomeMessageSetServiceImpl")
@RequiredArgsConstructor
public class DoctorWelcomeMessageSetServiceImpl extends ServiceImpl<DoctorWelcomeMessageSetMapper, DoctorWelcomeMessageSet>
        implements DoctorWelcomeMessageSetService {


}
