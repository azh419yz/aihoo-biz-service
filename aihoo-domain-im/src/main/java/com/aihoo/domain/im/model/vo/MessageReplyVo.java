package com.aihoo.domain.im.model.vo;

import com.aihoo.domain.im.model.entity.MessageReply;

import java.util.List;


public class MessageReplyVo extends MessageReply {
    private String nickName;

    private String headImg;

    private String recommendName;

    private String messageUpdateTime;

    private String name;

    private String messageWechatUserId;

    private List<MessageReplyVo> messageReviewList;

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

    public String getRecommendName() {
        return recommendName;
    }

    public void setRecommendName(String recommendName) {
        this.recommendName = recommendName;
    }

    public String getMessageUpdateTime() {
        return messageUpdateTime;
    }

    public void setMessageUpdateTime(String messageUpdateTime) {
        this.messageUpdateTime = messageUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageWechatUserId() {
        return messageWechatUserId;
    }

    public void setMessageWechatUserId(String messageWechatUserId) {
        this.messageWechatUserId = messageWechatUserId;
    }

    public List<MessageReplyVo> getMessageReviewList() {
        return messageReviewList;
    }

    public void setMessageReviewList(List<MessageReplyVo> messageReviewList) {
        this.messageReviewList = messageReviewList;
    }
}
