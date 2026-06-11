package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.vo.MdtPushMessageVo;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Classname PushMessageMapper
 * @Description hf
 * @Date 2020/9/25 14:41
 * @Created by ad
 */
public interface PushMessageMapper extends BaseMapper<PushMessage> {
    IPage<MdtPushMessageVo> mdtMessageList(Page<Object> objectPage, String doctorUserId, String type, String messageType, String orderNum, String startDate, String endDate, String content);
}
