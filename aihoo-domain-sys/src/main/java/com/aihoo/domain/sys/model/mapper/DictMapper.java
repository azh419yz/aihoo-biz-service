package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    List<Map<String, String>> selectByType(@Param("type") String type);
    Dict selectByCode(@Param("type") String type, @Param("code") String code);
}
