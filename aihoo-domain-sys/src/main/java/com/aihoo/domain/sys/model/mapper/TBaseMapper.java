package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.TBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TBaseMapper extends BaseMapper<TBase> {

    @Select("SELECT * FROM t_base WHERE `key` = #{key} AND is_delete = 0 LIMIT 1")
    TBase selectByKey(@Param("key") String key);
}
