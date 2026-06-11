package com.aihoo.api.doctor.app.controller.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/2 20:19
 */
@Data
@Schema(description = "im群消息发送")
public class ImSendGroupMsgRequest {
    @JSONField(name = "GroupId")
    @Schema(name = "groupId", description = "群ID")
    private String groupId;
    @JSONField(name = "From_Account")
    @Schema(name = "fromAccount", description = "发送人")
    private String fromAccount;
    @Schema(name = "toAccount", description = "接收人")
    private String toAccount;
    @JSONField(name = "Random")
    @Schema(name = "random", description = "随机数")
    private Long random;
    @JSONField(name = "MsgBody")
    @Schema(name = "msgBody", description = "消息内容")
    private List<MessageBody> msgBody;
    @JSONField(name = "CloudCustomData")
    @Schema(name = "cloudCustomData", description = "传递参数")
    private String cloudCustomData;
    @Schema(name = "visitNo", description = "订单号")
    private String visitNo;
    private Integer loadParam;
    private Integer msgType;

    @Data
    public static class MessageBody {
        @JSONField(name = "MsgType")
        private String msgType;
        @JSONField(name = "MsgContent")
        private MsgParam msgContent;
    }

    @Data
    public static class MsgParam {
        @JSONField(name = "Text")
        private String text;
        @JSONField(name = "Index")
        private Integer index;
        @JSONField(name = "Data")
        private String data;
    }
}
