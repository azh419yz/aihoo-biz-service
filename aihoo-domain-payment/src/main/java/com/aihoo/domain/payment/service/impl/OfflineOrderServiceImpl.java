package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.OfflineOrder;
import com.aihoo.domain.payment.model.mapper.OfflineOrderMapper;
import com.aihoo.domain.payment.service.OfflineOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflineOrderServiceImpl extends ServiceImpl<OfflineOrderMapper, OfflineOrder> implements OfflineOrderService {
}
