package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "中医证候视图对象")
public class TcmSyndromeVo {

    @Schema(description = "证候主键ID")
    private Long id;

    @Schema(description = "关联疾病ID")
    private Long diseaseId;

    @Schema(description = "证候名称")
    private String syndromeName;

    @Schema(description = "证候类型")
    private String syndromeType;
}
