package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("im_customer_msg")
public class ImCustomerMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String customerId;
    private String adminId;
    private String content;
    private String createTime;
    private String updateTime;
    private String createTimeStr;
    private String patientId;
    private String fromAccount;
    private String toAccount;
    private String msgSeq;
    private String msgRandom;
    private String msgTime;
    private String msgKey;
    private String sendMsgResult;
    private String errorInfo;
    private Integer direction;
    private String isRead;
    private String isFirst;
    private String mobile;
    private String avatar;
    private String name;
    private String conversationType = "C2C";

    @TableField(exist = false)
    private String time;
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private String from;
    @TableField(exist = false)
    private String to;
    @TableField(exist = false)
    private String data;
    @TableField(exist = false)
    private String msgContent;

    @TableField(exist = false)
    private List<ImMsgCustomerContent> payloads;

    @TableField(exist = false)
    private ImMsgCustomerContent payload;
}
