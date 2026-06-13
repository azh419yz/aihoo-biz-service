package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.model.entity.Message;
import com.aihoo.domain.im.model.mapper.MessageMapper;
import com.aihoo.domain.im.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {
}
