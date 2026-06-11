package com.aihoo.domain.im.model.mapper;

import com.aihoo.domain.im.model.vo.MessageReplyVo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by xieyc on 2020/7/31.
 */
public interface MessageReplyVoMapper extends BaseMapper<MessageReplyVo> {


    List<MessageReplyVo> getHomepageMessageReply(Map<String, Object> map);

    Integer getMessageReplyCount(Map<String, Object> map);

    List<MessageReplyVo> selectAllComment(Map map);

    List<MessageReplyVo> getMessageReply(Map<String, Object> map);

    //回复评论信息
    List<MessageReplyVo> getMessageReview(Map<String, Object> map);

    MessageReplyVo getMessageReplyDetail(Map<String, Object> map);

    List<MessageReplyVo> getUserMessageList(Map<String, Object> map);
}
