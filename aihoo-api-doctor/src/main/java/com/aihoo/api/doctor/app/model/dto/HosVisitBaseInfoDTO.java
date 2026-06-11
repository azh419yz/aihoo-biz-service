package com.aihoo.api.doctor.app.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "问诊资料-基本情况")
public class HosVisitBaseInfoDTO {

    @Schema(name = "hosSickId", description = "患者id", example = "123")
    private Long hosSickId;

    @Schema(name = "area", description = "患者地区编码", example = "110000,111000,111001")
    private String area;

    @Schema(name = "areaName", description = "患者地区中文", example = "北京北京市东城区")
    private String areaName;

    @Schema(name = "height", description = "身高", example = "155")
    private String height;

    @Schema(name = "weight", description = "体重", example = "150")
    private String weight;

    @Schema(name = "medicalHistory", description = "既往病史", example = "感冒")
    private String medicalHistory;

    @Schema(name = "allergyHistory", description = "过敏史", example = "花粉过敏")
    private String allergyHistory;

    @Schema(name = "desc", description = "病情描述", example = "头疼")
    private String desc;

    @Schema(name = "tongueImages", description = "舌照", example = "https://aihoo-static.oss-cn-wulanchabu.aliyuncs.com/patient_aihoo/files/202602112051345948595cb0dd4c68855da1e8e179656f.png")
    private List<String> tongueImages;

    @Schema(name = "faceImages", description = "面照", example = "https://aihoo-static.oss-cn-wulanchabu.aliyuncs.com/patient_aihoo/files/202602112051345948595cb0dd4c68855da1e8e179656f.png")
    private List<String> faceImages;

    @Schema(name = "medicalRecordImages", description = "病例", example = "https://aihoo-static.oss-cn-wulanchabu.aliyuncs.com/patient_aihoo/files/202602112051345948595cb0dd4c68855da1e8e179656f.png")
    private List<String> medicalRecordImages;
}
