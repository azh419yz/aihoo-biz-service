package com.aihoo.domain.im.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 提示信息表
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@TableName("t_message_warn")
public class MessageWarn implements Serializable {

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
     * 用户id
     */
    private String wechatUserId;

    /**
     * 类型 审核提示:CHECK 新消息:NEWS 新点赞:THUMP_UP
     */
    private String type;

    /**
     * 对应类型的表id
     */
    private String otherId;

    /**
     * 提示信息标题
     */
    private String title;

    /**
     * 提示内容
     */
    private String content;

    /**
     * 是否已读 1:已读 0:未读
     */
    private String isRead;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;


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

    public String getWechatUserId() {
        return wechatUserId;
    }

    public void setWechatUserId(String wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "MessageWarn{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", wechatUserId=" + wechatUserId +
        ", type=" + type +
        ", otherId=" + otherId +
        ", title=" + title +
        ", content=" + content +
        ", isRead=" + isRead +
        ", isDelete=" + isDelete +
        "}";
    }
}
