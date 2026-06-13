package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("im_msg_customer_content")
public class ImMsgCustomerContent implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String msgId;
    private String content;
    private String msgType;
    private String from;
    private String to;
    private String time;
    private String isDispose;
    private String msgContent;
    private String data;
}
