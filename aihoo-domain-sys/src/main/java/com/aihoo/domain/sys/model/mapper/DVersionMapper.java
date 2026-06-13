package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.DVersion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DVersionMapper extends BaseMapper<DVersion> {

    DVersion versionsUpdate(Map<String, Object> map);
}
