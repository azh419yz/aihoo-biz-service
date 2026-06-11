package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/4 16:33
 */
@Data
@Schema(description = "创蓝接口返回 VO")
public class ChuangLanLoginVo {

    /**
     * 200000表示成功，其他代码都为失败，详情参考附录。
     */
    @Schema(name = "code", description = "200000表示成功，其他代码都为失败，详情参考附录。", example = "200000")
    private String code;

    /**
     * 响应代码描述
     */
    @Schema(name = "message", description = "响应代码描述", example = "")
    private String message;
    /**
     * 是否收费，枚举值：1:收费/0:不收费
     */
    @Schema(name = "chargeStatus", description = "是否收费，枚举值：1:收费/0:不收费", example = "1")
    private Integer chargeStatus;

    @Schema(name = "data", description = "数据结果 json对象,如果本机校验，值为{tradeNo:xx,isVerify:xx}," +
            "否则为{mobileName:xx,tradeNo:xx}", example = "")
    private ResultData data;

    @Data
    @Schema(name = "ResultData", description = "创蓝接口返回", example = "1")
    public static class ResultData {
        /**
         * 手机号密文 ，根据传入的encryptType值选择对应算法解密手机号。
         */
        @Schema(name = "mobile", description = "手机号", example = "")
        private String mobile;
        /**
         * 闪验的交易流水号
         */
        @Schema(name = "tradeNo", description = "闪验的交易流水号", example = "")
        private String tradeNo;
        /**
         * 本机校验时使用
         * 值：1 是本机号码 0 非本机号码
         */
        @Schema(name = "isVerify", description = "本机校验时使用，值：1 是本机号码 0 非本机号码", example = "")
        private String isVerify;
    }
}
