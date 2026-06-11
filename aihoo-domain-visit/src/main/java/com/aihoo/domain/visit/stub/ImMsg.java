package com.aihoo.domain.visit.stub;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 占位实体：原 t_im_msg 表。
 * 字段集合取自 VisitOrderServiceImpl.imList / getImMsg 的使用面。
 * 待 im 域统一后改用 com.aihoo.domain.im.model.entity.ImMsg。
 */
@Data
@TableName("t_im_msg")
public class ImMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("create_time_str")
    private String createTimeStr;
    @TableField("from_account")
    private String fromAccount;
    @TableField("to_account")
    private String toAccount;
    @TableField("msg_seq")
    private String msgSeq;
    @TableField("msg_random")
    private String msgRandom;
    @TableField("msg_time")
    private String msgTime;
    @TableField("msg_key")
    private String msgKey;
    @TableField("send_msg_result")
    private String sendMsgResult;
    @TableField("error_info")
    private String errorInfo;
    @TableField("order_num")
    private String orderNum;
    @TableField("order_type")
    private String orderType;
}
