package com.aihoo.domain.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户查询请求参数")
public class SearchUserRequest {

    @Schema(description = "姓名", example = "老王")
    private String trueName;

    @Schema(description = "手机号码", example = "13800001111")
    private String phone;

}