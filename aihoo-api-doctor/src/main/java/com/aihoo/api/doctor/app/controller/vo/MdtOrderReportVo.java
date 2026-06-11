package com.aihoo.api.doctor.app.controller.vo;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MdtOrderReportVo implements Serializable {

    @Schema(description = "会诊订单号")
    @TableField("order_num")
    private String orderNum;

    private String mdtAppointmentTime;//预约时间

    private String mdtStartTime;//会诊开始时间

    private String mdtEndTime;//会诊结束时间

    private String sickName;//患者姓名

    private String sex;//性别

    private String sexName;//性别(文字)

    private String age;//年龄

    private String consultationDoctor;//会诊医生

    private String reportAuthor;

    private List<String> signList;

    private String reportUrl;

    private String isImg;

    /**
     * 会诊要求
     */
    private String demand;

    /**
     * 检查报告
     */
    private String checkup;

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


}
