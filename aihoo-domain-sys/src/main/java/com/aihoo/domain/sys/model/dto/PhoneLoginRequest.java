package com.aihoo.domain.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "手机验证码登录请求对象")
public class PhoneLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号格式错误")
    private String phone;

    @Schema(description = "短信验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "88888888")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
