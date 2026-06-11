package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.entity.MessageFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 发布信息图片视频表 Mapper 接口
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface MessageFileMapper extends BaseMapper<MessageFile> {
    MessageFile getMessageFile(Map<String,Object> map);


}
