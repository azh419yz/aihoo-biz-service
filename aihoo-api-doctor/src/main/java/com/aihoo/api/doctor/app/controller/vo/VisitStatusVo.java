package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/11 09:56
 */
@Data
@Schema(description = "状态上下级")
public class VisitStatusVo {
    @Schema(description = "目前状态")
    String now;
    @Schema(description = "上一级的状态")
    List<String> before;
    @Schema(description = "下一级的状态")
    List<String> after;
}
