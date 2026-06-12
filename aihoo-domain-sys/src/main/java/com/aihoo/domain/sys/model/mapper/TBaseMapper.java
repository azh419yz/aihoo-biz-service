package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.TBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 倒计时
 */
public interface TBaseMapper extends BaseMapper<TBase> {
    TBase selectByKey(String key);
}
