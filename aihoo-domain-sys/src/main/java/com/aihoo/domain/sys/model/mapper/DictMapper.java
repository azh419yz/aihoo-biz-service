package com.aihoo.domain.sys.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.domain.sys.model.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/22 8:40
 * @description：字典
 */
public interface DictMapper extends BaseMapper<Dict> {

    List<Map<String, String>> selectByType(String type);

    Dict selectByCode(String type, String code);
}