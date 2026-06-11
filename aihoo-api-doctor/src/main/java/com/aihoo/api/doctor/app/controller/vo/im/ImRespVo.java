package com.aihoo.api.doctor.app.controller.vo.im;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/2 17:23
 */
@Data
@Schema(name = "ImRespVo", description = "im信息发送回参")
public class ImRespVo {
    /**
     * 请求处理的结果：
     * OK：表示处理成功。
     * FAIL：表示失败。
     */
    @JSONField(name = "ActionStatus")
    @Schema(description = "请求处理的结果 OK：表示处理成功。FAIL：表示失败。", example = "OK")
    private String actionStatus;
    /**
     * 错误信息。
     */
    @JSONField(name = "ErrorInfo")
    @Schema(description = "错误信息")
    private String errorInfo;
    /**
     * 错误码：
     * 0：表示成功。
     * 非0：表示失败。
     */
    @JSONField(name = "ErrorCode")
    @Schema(description = "错误码 0：表示成功。非0：表示失败。", example = "0")
    private Integer errorCode;

    public boolean isSuccess() {
        return "OK".equals(actionStatus) || (errorCode != null && errorCode == 0);
    }

    public ImRespVo success() {
        ImRespVo imResponseVo = new ImRespVo();
        imResponseVo.setActionStatus("OK");
        imResponseVo.setErrorCode(0);
        return imResponseVo;
    }

    public ImRespVo fail(Integer errorCode, String errorInfo) {
        ImRespVo imResponseVo = new ImRespVo();
        imResponseVo.setActionStatus("FAIL");
        imResponseVo.setErrorCode(errorCode);
        imResponseVo.setErrorInfo(errorInfo);
        return imResponseVo;
    }

    public ImRespVo fail() {
        return fail(200, "未知错误");
    }
}
