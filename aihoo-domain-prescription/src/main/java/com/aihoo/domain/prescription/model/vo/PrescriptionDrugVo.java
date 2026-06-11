package com.aihoo.domain.prescription.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "处方笺药品VO")
public class PrescriptionDrugVo {

    @Schema(name = "name", description = "药名", example = "三七")
    private String name;

    @Schema(name = "number", description = "总克重", example = "10")
    private String number;

    @Schema(name = "method", description = "使用方式", example = "后入")
    private String method;

}
