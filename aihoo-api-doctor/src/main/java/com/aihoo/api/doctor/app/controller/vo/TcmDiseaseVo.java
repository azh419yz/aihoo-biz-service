package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "中医疾病视图对象")
public class TcmDiseaseVo {

    @Schema(description = "疾病主键ID")
    private Long id;

    @Schema(description = "病名")
    private String diseaseName;

    @Schema(description = "病名拼音首字母")
    private String diseasePinyinInitial;

    @Schema(description = "疾病分类")
    private String diseaseCategory;
}
