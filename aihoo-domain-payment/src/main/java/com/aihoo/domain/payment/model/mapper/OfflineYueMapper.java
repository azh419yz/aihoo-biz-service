package com.aihoo.domain.payment.model.mapper;

import com.aihoo.domain.payment.model.entity.OfflineYue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @ClassName: OfflineYueMapper .class
 * @author: gjk
 * @create: 2021-04-13 18:38
 **/
public interface OfflineYueMapper extends BaseMapper<OfflineYue> {
    OfflineYue selectOderYue();
}