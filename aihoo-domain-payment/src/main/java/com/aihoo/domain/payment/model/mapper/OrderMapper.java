package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
