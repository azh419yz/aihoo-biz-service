package com.aihoo.domain.im.model.vo;

import com.aihoo.domain.im.model.entity.MessageWarn;


public class MessageWarnVo extends MessageWarn {
    private String state;
    private String updateTime;
    private String messageReplyId;
    private String messageId;
    private String messageReplycontent;
    private String recommendReplyId;
    private String nickName;
    private String headImg;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMessageReplyId() {
        return messageReplyId;
    }

    public void setMessageReplyId(String messageReplyId) {
        this.messageReplyId = messageReplyId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageReplycontent() {
        return messageReplycontent;
    }

    public void setMessageReplycontent(String messageReplycontent) {
        this.messageReplycontent = messageReplycontent;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRecommendReplyId() {
        return recommendReplyId;
    }

    public void setRecommendReplyId(String recommendReplyId) {
        this.recommendReplyId = recommendReplyId;
    }

}
