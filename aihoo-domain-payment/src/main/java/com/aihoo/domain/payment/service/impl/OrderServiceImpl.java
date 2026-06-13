package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.service.OrderService;
import com.aihoo.domain.payment.model.entity.Order;
import org.springframework.stereotype.Service;
import com.aihoo.domain.payment.model.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
