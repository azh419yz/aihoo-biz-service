package com.aihoo.domain.payment.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Classname OrderServiceMapper
 * @Description hf
 * @Date 2020/9/22 16:43
 * @Created by ad
 */
public interface OrderServiceMapper extends BaseMapper<Object> {
    Object getInquiryDetails(@Param("id") String id);
}