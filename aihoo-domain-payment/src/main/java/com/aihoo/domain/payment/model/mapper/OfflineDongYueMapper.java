package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.entity.OfflineDongYue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @ClassName: OfflineDongYueMapper .class
 * @author: gjk
 * @create: 2021-04-22 15:36
 **/
public interface OfflineDongYueMapper extends BaseMapper<OfflineDongYue> {

    OfflineDongYue selectOderYue();
}