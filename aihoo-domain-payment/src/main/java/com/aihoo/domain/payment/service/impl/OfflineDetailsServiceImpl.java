package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.OfflineDetails;
import com.aihoo.domain.payment.model.mapper.OfflineDetailsMapper;
import com.aihoo.domain.payment.service.OfflineDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflineDetailsServiceImpl extends ServiceImpl<OfflineDetailsMapper, OfflineDetails> implements OfflineDetailsService {
}
