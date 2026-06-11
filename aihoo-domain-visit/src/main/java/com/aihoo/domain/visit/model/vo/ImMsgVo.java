package com.aihoo.domain.visit.model.vo;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ImMsgVo implements Serializable {



    @Schema(description = "消息创建时间，单位为秒")
    @TableField("create_time_str")
    private String createTimeStr;

    @Schema(description = "发送者用户id")
    @TableField("from_account")
    private String fromAccount;

    @Schema(description = "接收者用户id")
    @TableField("to_account")
    private String toAccount;

    @Schema(description = "消息序列号")
    @TableField("msg_seq")
    private String msgSeq;

    @Schema(description = "消息随机数")
    @TableField("msg_random")
    private String msgRandom;

    @Schema(description = "消息的发送时间戳 单位为秒")
    @TableField("msg_time")
    private String msgTime;

    @Schema(description = "该条消息的唯一标识，可根据该标识进行 REST API 撤回单聊消息")
    @TableField("msg_key")
    private String msgKey;

    @Schema(description = "该条消息的下发结果，0表示下发成功，非0表示下发失败")
    @TableField("send_msg_result")
    private String sendMsgResult;

    @Schema(description = "该条消息下发失败的错误信息，若消息发送成功，则为send msg succeed")
    @TableField("error_info")
    private String errorInfo;

    @Schema(description = "订单号")
    @TableField("order_num")
    private String orderNum;

    @Schema(description = "订单类型")
    @TableField("order_type")
    private String orderType;

    private List<ImMsgContentVo> payloads;//消息内容
    private ImMsgContentVo payload;//消息内容

    private String time;
    private String type;
    private String from;
    private String to;
    private String conversationType = "C2C";
    private String avatar;
}
