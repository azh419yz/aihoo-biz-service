package com.aihoo.api.doctor.app.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/6 16:20
 */
@Data
@Schema(description = "开方请求")
public class PrescriptionQueryRequest {
    private Integer page;
    private Integer limit;
}
