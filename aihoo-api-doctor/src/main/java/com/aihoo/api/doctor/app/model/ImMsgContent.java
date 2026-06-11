package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * IM消息内容表
 * </p>
 *
 * @author zys
 * @since 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_im_msg_content")
@Schema(description = "IM消息内容表")
public class ImMsgContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息内容主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private String updateTime;

    @Schema(description = "创建时间，单位为秒")
    @TableField("create_time_str")
    private String createTimeStr;

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
