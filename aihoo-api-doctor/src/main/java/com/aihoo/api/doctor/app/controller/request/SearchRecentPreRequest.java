package com.aihoo.api.doctor.app.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "查询最近处方请求")
public class SearchRecentPreRequest {

    @Schema(name = "hosSickId", description = "就诊人ID", example = "1")
    @NotBlank(message = "就诊人ID不能为空")
    private String hosSickId;

    @Schema(name = "doctorUserId", description = "医生ID", example = "1")
    @NotBlank(message = "医生ID不能为空")
    private String doctorUserId;
}
