package com.aihoo.api.admin.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "处方笺VO")
public class HosPrescriptionInnerVo {

    @Schema(name = "id", description = "处方ID", example = "1")
    private String id;

    @Schema(name = "createTime", description = "创建（开方）时间", example = "2026-03-03 10:00:00")
    private String createTime;

    @Schema(name = "name", description = "姓名", example = "张三")
    private String name;

    @Schema(name = "sex", description = "性别", example = "男")
    private String sex;

    @Schema(name = "age", description = "年龄", example = "19")
    private String age;

    @Schema(name = "advice", description = "医嘱", example = "少喝热水")
    private String advice;

    @Schema(name = "diseaseSyndrome", description = "辨病辨证", example = "湿气重")
    private String diseaseSyndrome;

    @Schema(name = "departName", description = "科室", example = "中医内科")
    private String departName;

    @Schema(name = "drugstoreName", description = "药房名称", example = "同仁堂")
    private String drugstoreName;

    @Schema(name = "medicineStatusCode", description = "药态", example = "内服颗粒")
    private String medicineStatusCode;

    @Schema(name = "drugVoList", description = "药品")
    private List<PrescriptionDrugVo> drugVoList;

    @Schema(name = "method", description = "用法用量", example = "共14剂，每日1剂，分3次服用")
    private String method;

    @Schema(name = "receiveMsg", description = "收货信息", example = "姓名 电话 地址")
    private String receiveMsg;

    @Schema(name = "hosSickRemark", description = "患者备注", example = "加急")
    private String hosSickRemark;

    @Schema(name = "doctorName", description = "医生姓名", example = "华佗")
    private String doctorName;

    @Schema(name = "checkDoctorName", description = "审核医生姓名", example = "李时珍")
    private String checkDoctorName;

    @Schema(name = "allocateDoctorName", description = "调配医生姓名", example = "扁鹊")
    private String allocateDoctorName;

}
