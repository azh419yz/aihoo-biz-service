package com.aihoo.api.doctor.app.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aihoo.api.doctor.app.model.DVersion;

import java.util.Map;

/**
 * <p>
 * APP版本 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-11-06
 */
public interface DVersionMapper extends BaseMapper<DVersion> {

    DVersion versionsUpdate(Map<String, Object> map);
}
