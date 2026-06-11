package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname ImCustomerMsg
 * @Description hf
 * @Date 2020/11/10 15:40
 * @Created by ad
 */
@Data
@TableName("t_im_customer_msg")
public class ImCustomerMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 消息主键ID
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
     * 创建时间，单位为秒
     */
    private String createTimeStr;

    /**
     * 客服id
     */
    private String adminId;

    /**
     * 患者id
     */
    private String patientId;

    /**
     * 发送者用户id
     */
    private String fromAccount;

    /**
     * 接收者用户id
     */
    private String toAccount;

    /**
     * 消息序列号
     */
    private String msgSeq;

    /**
     * 消息随机数
     */
    private String msgRandom;

    /**
     * 消息的发送时间戳 单位为秒
     */
    private String msgTime;

    /**
     * 该条消息的唯一标识，可根据该标识进行 REST API 撤回单聊消息
     */
    private String msgKey;

    /**
     * 该条消息的下发结果，0表示下发成功，非0表示下发失败
     */
    private String sendMsgResult;

    /**
     * 该条消息下发失败的错误信息，若消息发送成功，则为send msg succeed
     */
    private String errorInfo;


    @TableField(exist = false)
    private List<ImMsgCustomerContent> payloads;
    @TableField(exist = false)
    private ImMsgCustomerContent payload;

    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private String from;
    @TableField(exist = false)
    private String to;
    @TableField(exist = false)
    private String conversationType = "C2C";
    @TableField(exist = false)
    private String avatar;

}
