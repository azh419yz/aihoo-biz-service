package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.OfflineAppointment;
import com.aihoo.domain.payment.model.mapper.OfflineAppointmentMapper;
import com.aihoo.domain.payment.service.OfflineAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflineAppointmentServiceImpl extends ServiceImpl<OfflineAppointmentMapper, OfflineAppointment> implements OfflineAppointmentService {
}
