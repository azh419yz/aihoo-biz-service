package com.aihoo.domain.im.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/17 20:53
 */
@Data
@Schema(description = "im消息撤回")
public class ImWithdrawMsgRequest {
    @NotNull(message = "群组ID不能为空")
    @Schema(name = "groupId", description = "群组ID")
    private String groupId;
    @NotNull(message = "消息唯一值不能为空")
    @Schema(name = "msgReq", description = "唯一值")
    private String msgReq;
}
