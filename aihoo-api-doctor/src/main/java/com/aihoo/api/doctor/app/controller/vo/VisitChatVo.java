package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "问诊复诊返回 VO")
public class VisitChatVo {

    @Schema(name = "msg", description = "返回信息", example = "开始在线复诊")
    private String msg;

    @Schema(name = "orderStatus", description = "订单状态 DONE订单关闭 CANCEL取消订单 DECLINE拒单 WAIT待付款 PAY已付款|待接单 HAVE已接单 END订单完成", example = "HAVE")
    private String orderStatus;

    @Schema(name = "cutDown", description = "问诊时间", example = "1")
    private String cutDown;

    @Schema(name = "isCanChat", description = "是否可以咨询 1:是 0:否", example = "1")
    private String isCanChat;
}
