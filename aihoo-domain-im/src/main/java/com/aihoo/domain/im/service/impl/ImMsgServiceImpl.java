package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.model.entity.ImMsg;
import com.aihoo.domain.im.model.mapper.ImMsgMapper;
import com.aihoo.domain.im.service.ImMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("imMsgServiceImpl")
public class ImMsgServiceImpl extends ServiceImpl<ImMsgMapper, ImMsg> implements ImMsgService {
}
