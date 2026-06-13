package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order_report")
public class MdtOrderReport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "会诊报告主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "会诊订单号")
    @TableField("order_num")
    private String orderNum;

    @Schema(description = "病史摘要")
    @TableField("medical_history_summary")
    private String medicalHistorySummary;

    @Schema(description = "会诊摘要")
    @TableField("consultation_summary")
    private String consultationSummary;

    @Schema(description = "诊断结果")
    @TableField("diagnosis_results")
    private String diagnosisResults;

    @Schema(description = "治疗方案")
    @TableField("treatment_plan")
    private String treatmentPlan;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private String updateTime;

    @Schema(description = "梅清给的图片地址")
    @TableField("img_path")
    private String imgPath;

    @Schema(description = "上传oss后的图片地址")
    @TableField("oss_img_path")
    private String ossImgPath;

    @Schema(description = "URLEncoder后的用户签章（URLEncoder，utf-8)")
    @TableField("seal")
    private String seal;

    @Schema(description = "URLEncoder后的压缩用户签章")
    @TableField("scale_seal")
    private String scaleSeal;

    @Schema(description = "会诊要求")
    @TableField("demand")
    private String demand;

    @Schema(description = "检查报告")
    @TableField("checkup")
    private String checkup;

    @Schema(description = "编写报告order_doctor_Id")
    @TableField("order_doctor_id")
    private String orderDoctorId;

    @Schema(description = "医生姓名")
    @TableField("doctor_name")
    private String doctorName;
}
