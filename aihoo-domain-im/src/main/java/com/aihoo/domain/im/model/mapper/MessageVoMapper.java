package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.vo.MessageVo;

import java.util.Map;

public interface MessageVoMapper {
    MessageVo getMessageVo(Map<String, Object> map);
}
