package com.aihoo.domain.prescription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/18 20:27
 */
@Data
@Schema(description = "撤回处方请求")
public class WithdrawPrescriptionRequest {
    @Schema(description = "处方ID")
    private Long PrescriptionId;
}
