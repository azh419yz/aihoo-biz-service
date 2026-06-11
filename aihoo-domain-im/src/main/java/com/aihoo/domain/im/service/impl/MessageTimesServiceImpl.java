package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.model.entity.MessageTimes;
import com.aihoo.domain.im.model.mapper.MessageTimesMapper;
import com.aihoo.domain.im.service.MessageTimesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 信息关注点赞浏览表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@Service
public class MessageTimesServiceImpl extends ServiceImpl<MessageTimesMapper, MessageTimes> implements MessageTimesService {

}
