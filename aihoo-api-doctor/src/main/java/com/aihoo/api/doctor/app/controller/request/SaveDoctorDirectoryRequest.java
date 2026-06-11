package com.aihoo.api.doctor.app.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/5 16:05
 */
@Data
@Schema(description = "保存医生通讯录请求")
public class SaveDoctorDirectoryRequest {
    @Schema(name = "sickId", description = "就诊人id")
    private Long sickId;
    @Schema(name = "patientUserId", description = "患者id")
    private Long patientUserId;
    @Schema(name = "doctorId", description = "医生id")
    private Long doctorId;
    @Schema(name = "sickName", description = "患者名称")
    private String sickName;
}
