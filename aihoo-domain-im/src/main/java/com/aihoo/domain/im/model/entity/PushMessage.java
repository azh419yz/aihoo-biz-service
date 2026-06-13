package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息推送表
 */
@Data
@TableName("t_push_message")
public class PushMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String createUserId;
    private String type;
    private String pesronalId;
    private String title;
    private String intro;
    private String messageType;
    private String otherId;
    private String img;
    private String isImg;
    private String content;
    private String isPush;
    private String isDelete;
    private String noticeType;
    private String pushTime;
    private String setTime;
}
