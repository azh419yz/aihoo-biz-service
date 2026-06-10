package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @Classname DiceMapper
 * @Description hf
 * @Date 2020/9/15 21:24
 * @Created by ad
 */
public interface DiceMapper extends BaseMapper<Dict> {
    List<Map> selectByType(String type);
    Dict selectByCode(String type,String code);
}
