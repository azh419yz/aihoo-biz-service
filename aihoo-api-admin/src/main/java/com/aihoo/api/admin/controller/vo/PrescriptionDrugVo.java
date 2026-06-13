package com.aihoo.api.admin.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrescriptionDrugVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "药品ID")
    private String drugId;
    @Schema(description = "药品名称")
    private String drugName;
    @Schema(description = "数量")
    private Integer quantity;
}
