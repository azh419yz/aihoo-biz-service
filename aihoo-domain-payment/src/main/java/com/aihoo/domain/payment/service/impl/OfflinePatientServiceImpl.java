package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.OfflinePatient;
import com.aihoo.domain.payment.model.mapper.OfflinePatientMapper;
import com.aihoo.domain.payment.service.OfflinePatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflinePatientServiceImpl extends ServiceImpl<OfflinePatientMapper, OfflinePatient> implements OfflinePatientService {
}
