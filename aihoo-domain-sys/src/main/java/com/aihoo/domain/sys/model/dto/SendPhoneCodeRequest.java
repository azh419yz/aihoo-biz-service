package com.aihoo.domain.sys.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Schema(description = "发送手机验证码请求对象")
public class SendPhoneCodeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号格式错误")
    private String phone;
}
