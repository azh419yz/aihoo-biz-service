package com.aihoo.domain.patient.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * 微信用户信息表
 * </p>
 *
 * @author mcp
 * @since 2020-07-24
 */
@TableName("t_wechat_user")
public class WechatUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headImg;

    private String oldImg;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 是否登录 1：登录0登出
     */
    private Boolean isLogin;

    /**
     * 是否启用 1:启用 0:停用
     */
    private Boolean state;

    /**
     * 阶段停用-开始时间
     */
    private String startTime;

    /**
     * 阶段停用-结束时间
     */
    private String endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getOldImg() {
        return oldImg;
    }

    public void setOldImg(String oldImg) {
        this.oldImg = oldImg;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WechatUser{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", nickName=" + nickName +
        ", headImg=" + headImg +
        ", oldImg=" + oldImg +
        ", mobile=" + mobile +
        ", openId=" + openId +
        ", isLogin=" + isLogin +
        ", state=" + state +
        "}";
    }
}
