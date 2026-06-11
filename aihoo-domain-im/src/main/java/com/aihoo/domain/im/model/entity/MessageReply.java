package com.aihoo.domain.im.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 信息回复表
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@TableName("t_message_reply")
public class MessageReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 用户id
     */
    private Long wechatUserId;

    /**
     * 信息外键id
     */
    private Long messageId;

    /**
     * 信息回复上级id
     */
    private Long messageReplyId;

    /**
     * 上级用户id
     */
    private Long recommendId;

    /**
     * 信息回复上级回复id
     */
    private Long recommendReplyId;

    /**
     * 回复信息
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getWechatUserId() {
        return wechatUserId;
    }

    public void setWechatUserId(Long wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageReplyId() {
        return messageReplyId;
    }

    public void setMessageReplyId(Long messageReplyId) {
        this.messageReplyId = messageReplyId;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public Long getRecommendReplyId() {
        return recommendReplyId;
    }

    public void setRecommendReplyId(Long recommendReplyId) {
        this.recommendReplyId = recommendReplyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageReply{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", wechatUserId=" + wechatUserId +
        ", messageId=" + messageId +
        ", messageReplyId=" + messageReplyId +
        ", recommendId=" + recommendId +
        ", recommendReplyId=" + recommendReplyId +
        ", content=" + content +
        "}";
    }
}
