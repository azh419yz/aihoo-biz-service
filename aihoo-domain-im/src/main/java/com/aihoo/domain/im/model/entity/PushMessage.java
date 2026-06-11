package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname PushMessage
 * @Description hf
 * @Date 2020/9/25 11:02
 * @Created by ad
 */
@Data
@TableName("t_push_message")
public class PushMessage implements Serializable {
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
     * 创建人id
     */
    private String createUserId;

    /**
     * 通知类型 ALL-所有人 PATIENT-患者 DOCKER-医生 PATIENT_PERSONAL-患者个人 DOCKER_PERSONAL-医生个人
     */
    private String type;

    /**
     * 消息第三方ID 更加消息类型 推送人ID PATIENT_PERSONAL t_patient_user外键ID DOCKER_PERSONAL t_doctor_user外键ID
     */
    private String pesronalId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息简介
     */
    private String intro;

    /**
     * 消息类型 TEXT-文字公告 IMAGE_TEXT-图文公告 IMAGE-图文 VOICE-语音 VIDEO-视频 REVISIT-复诊
     */
    private String messageType;

    /**
     * 消息第三方ID 更加消息类型 不同的消息详情ID 【IMAGE VOICE VIDEO】t_hos_visit外键ID REVISIT t_hos_revisit外键ID
     */
    private String otherId;

    /**
     * 消息图片
     */
    private String img;

    /**
     * 是否有图片 1:有 0:无
     */
    private String isImg;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已推送 1:已推送 0:未推送
     */
    private String isPush;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

    /**
     * 通知类型 SYSTEM-系统公告 SERVICE-服务提醒
     */
    private String noticeType;

    /**
     * 推送时间
     */
    private String pushTime;

    /**
     * 目标推送时间
     */
    private String setTime;

    /**
     * 目标推送时间
     */
    private String isRead;
}
