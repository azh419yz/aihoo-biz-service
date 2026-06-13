package com.aihoo.domain.im.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/2 20:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ImSendGroupMsgRespVo", description = "im群发普通消息")
public class ImSendGroupMsgRespVo extends ImRespVo {
    @Schema(description = "消息时间戳")
    @JSONField(name = "MsgTime")
    private Long msgTime;
    @Schema(description = "32位序列号")
    @JSONField(name = "MsgSeq")
    private Long msgSeq;
}
