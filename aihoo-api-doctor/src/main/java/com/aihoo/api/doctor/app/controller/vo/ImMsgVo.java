package com.aihoo.api.doctor.app.controller.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

    @Schema(description = "消息类型 1:普通消息 2:系统消息")
    @TableField("msg_type")
    private String msgType;

    private List<ImMsgContentVo> msgContents;//消息内容
}
