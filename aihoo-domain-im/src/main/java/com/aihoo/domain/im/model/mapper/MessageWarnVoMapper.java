package com.aihoo.domain.im.model.mapper;


import com.aihoo.domain.im.model.vo.MessageWarnVo;
import com.aihoo.domain.im.model.entity.MessageWarn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提示信息表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-08-05
 */
public interface MessageWarnVoMapper extends BaseMapper<MessageWarn> {

    List<MessageWarnVo> getMessagePrompt(Map<String, Object> map);

    List<MessageWarnVo> getNewMessage(Map<String, Object> map);


}
