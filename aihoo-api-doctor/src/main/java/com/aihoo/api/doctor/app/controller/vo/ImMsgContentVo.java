package com.aihoo.api.doctor.app.controller.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ImMsgContentVo implements Serializable {

    @Schema(description = "消息类型 TIMTextElem(文本消息)，TIMLocationElem(位置消息)，TIMFaceElem(表情消息)，TIMCustomElem(自定义消息)，TIMSoundElem(语音消息)，TIMImageElem(图像消息)，TIMFileElem(文件消息)，TIMVideoFileElem(视频消息)")
    @TableField("msg_type")
    private String msgType;

    @Schema(description = "消息类型名称")
    @TableField("msg_type_name")
    private String msgTypeName;

    @Schema(description = "消息内容")
    @TableField("msg_content")
    private String msgContent;

    @Schema(description = "消息主键ID")
    @TableField("im_msg_id")
    private String imMsgId;
}
