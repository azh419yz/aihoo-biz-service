package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DrugVo {
    /**
     * 主键ID
     */
    private String id;

    @Schema(name = "name", description = "药品名称")
    private String name;

    @Schema(name = "price", description = "药品单价")
    private String price;

    @Schema(name = "method", description = "煎药方式")
    private String method;

}
