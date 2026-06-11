package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.entity.MessageWarn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 提示信息表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface MessageWarnMapper extends BaseMapper<MessageWarn> {
    void updateReadByMessageId(Map<String, Object> map);

    void updateMessageWarn(Map<String,Object> map);
}
