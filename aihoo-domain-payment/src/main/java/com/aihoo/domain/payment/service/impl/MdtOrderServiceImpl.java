package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.MdtOrder;
import com.aihoo.domain.payment.model.mapper.MdtOrderMapper;
import com.aihoo.domain.payment.service.MdtOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MdtOrderServiceImpl extends ServiceImpl<MdtOrderMapper, MdtOrder> implements MdtOrderService {
}
