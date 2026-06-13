package com.aihoo.domain.patient.dto;

import com.aihoo.domain.visit.model.vo.HosVisitVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "患者信息 VO")
public class HosSickVo {

    @Schema(name = "id", description = "主键ID", example = "1")
    private String id;
    @Schema(name = "avatar", description = "头像", example = "")
    private String avatar;
    @Schema(name = "name", description = "姓名", example = "张三")
    private String name;
    @Schema(name = "mobile", description = "手机号码", example = "1")
    private String mobile;
    @Schema(name = "idCardVerify", description = "实名制认证 1:通过 0:不通过", example = "1")
    private Integer idCardVerify;
    @Schema(name = "idCard", description = "证件信息", example = "1")
    private String idCard;
    @Schema(name = "sex", description = "性别 0-女 1-男", example = "1")
    private String sex;
    @Schema(name = "age", description = "年龄", example = "18")
    private String age;
    @Schema(name = "height", description = "身高", example = "1")
    private String height;
    @Schema(name = "weight", description = "体重", example = "1")
    private String weight;
    @Schema(name = "area", description = "地区", example = "1")
    private String area;
    @Schema(name = "pastHistory", description = "既往史", example = "1")
    private String pastHistory;
    @Schema(name = "allergyHistory", description = "过敏史", example = "1")
    private String allergyHistory;
    @Schema(name = "desc", description = "病情描述", example = "1")
    private String desc;
    @Schema(name = "hosVisits", description = "问诊卡列表", example = "1")
    private List<HosVisitVo> hosVisits;
    @Schema(name = "tongueImages", description = "舌照", example = "")
    private List<String> tongueImages;
    @Schema(name = "faceImages", description = "面照", example = "")
    private List<String> faceImages;
    @Schema(name = "medicalRecordImages", description = "病例", example = "")
    private List<String> medicalRecordImages;

}
