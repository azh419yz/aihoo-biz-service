package com.aihoo.api.doctor.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.TBase;

/**
 * 倒计时
 */
public interface TBaseMapper extends BaseMapper<TBase> {
    TBase selectByKey(String key);
}