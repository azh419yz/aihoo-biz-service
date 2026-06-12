package com.aihoo.api.doctor.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.prescription.model.mapper.HosPrescriptionMapper;
import com.aihoo.api.doctor.app.model.HosPrescription;
import com.aihoo.api.doctor.app.service.HosPrescriptionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/6 16:02
 */
@Service("doctorApiHosPrescriptionServiceImpl")
public class HosPrescriptionServiceImpl extends ServiceImpl<HosPrescriptionMapper, HosPrescription>
        implements HosPrescriptionService {
}
