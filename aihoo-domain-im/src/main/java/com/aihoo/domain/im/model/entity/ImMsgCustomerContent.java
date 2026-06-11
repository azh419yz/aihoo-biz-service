package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname ImMsgCustomerContent
 * @Description hf
 * @Date 2020/11/10 15:42
 * @Created by ad
 */
@Data
@TableName("t_im_msg_customer_content")
public class ImMsgCustomerContent implements Serializable {
    /**
     * 消息内容主键ID
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
     * 消息类型 TIMTextElem(文本消息)，TIMLocationElem(位置消息)，TIMFaceElem(表情消息)，TIMCustomElem(自定义消息)，TIMSoundElem(语音消息)，TIMImageElem(图像消息)，TIMFileElem(文件消息)，TIMVideoFileElem(视频消息)
     */
    private String msgType;

    /**
     * 消息类型名称
     */
    private String msgTypeName;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息内容
     */
    @TableField(exist = false)
    private String data;

    /**
     * 消息主键ID
     */
    private String imMsgId;

    private static final long serialVersionUID = 1L;
}
