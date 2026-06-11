package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_im_msg_content 表。
 * 字段集合取自 VisitOrderServiceImpl.getImMsg 的使用面。
 * 待 im 域统一后改用 com.aihoo.domain.im.model.entity.ImMsgContent。
 */
@Data
@TableName("t_im_msg_content")
public class ImMsgContent implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("msg_type")
    private String msgType;
    @TableField("msg_type_name")
    private String msgTypeName;
    @TableField("msg_content")
    private String msgContent;
    @TableField("data")
    private String data;
    @TableField("im_msg_id")
    private String imMsgId;
}
