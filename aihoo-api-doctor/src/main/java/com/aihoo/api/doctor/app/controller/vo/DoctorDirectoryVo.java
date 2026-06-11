package com.aihoo.api.doctor.app.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/5 16:10
 */
@Data
@Schema(description = "医生通讯录 VO")
public class DoctorDirectoryVo {
    @Schema(name = "avatar", description = "头像")
    private String avatar;
    @Schema(name = "sickName", description = "名称")
    private String sickName;
    @Schema(name = "sickId", description = "病人id")
    private Long sickId;
    @Schema(name = "patientUserId", description = "用户id")
    private Long patientUserId;
    @Schema(name = "sickSex", description = "性别")
    private String sickSex;
    @Schema(name = "sickAge", description = "年龄")
    private String sickAge;
    @Schema(name = "mobile", description = "手机号")
    private String mobile;
    @Schema(name = "saveTime", description = "添加时间")
    private String saveTime;
    @Schema(name = "source", description = "来源 1:扫码  2:购买问诊卡", example = "1")
    private Integer source;

}
