package com.aihoo.domain.im.model.vo;

import com.aihoo.domain.im.model.entity.Message;

public class MessageVo extends Message {
    private String userName;

    private String fileType;

    private String fileUrl;

    private String nickName;
    private String headImg;
    private Integer isCare;
    private Integer isThumpUp;
    private String distanceUm;
    private String messageTimesId;

    private String categoryId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public Integer getIsCare() {
        return isCare;
    }

    public void setIsCare(Integer isCare) {
        this.isCare = isCare;
    }

    public Integer getIsThumpUp() {
        return isThumpUp;
    }

    public void setIsThumpUp(Integer isThumpUp) {
        this.isThumpUp = isThumpUp;
    }

    public String getDistanceUm() {
        return distanceUm;
    }

    public void setDistanceUm(String distanceUm) {
        this.distanceUm = distanceUm;
    }

    public String getMessageTimesId() {
        return messageTimesId;
    }

    public void setMessageTimesId(String messageTimesId) {
        this.messageTimesId = messageTimesId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
