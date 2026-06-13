package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.model.entity.ImMsgContent;
import com.aihoo.domain.im.model.mapper.ImMsgContentMapper;
import com.aihoo.domain.im.service.ImMsgContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("imMsgContentServiceImpl")
public class ImMsgContentServiceImpl extends ServiceImpl<ImMsgContentMapper, ImMsgContent>
        implements ImMsgContentService {
}
