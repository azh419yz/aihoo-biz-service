package com.aihoo.api.doctor.app.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.im.model.mapper.ImMsgMapper;
import com.aihoo.domain.im.model.entity.ImMsg;
import com.aihoo.api.doctor.app.service.ImMsgService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * IM消息表 服务实现类
 * </p>
 *
 * @author zys
 * @since 2020-10-15
 */
@Service
public class ImMsgServiceImpl extends ServiceImpl<ImMsgMapper, ImMsg> implements ImMsgService {
}
