package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order_report_audit")
public class MdtOrderReportAudit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "会诊审核记录ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(description = "审核医生医院")
    @TableField("audit_hospital_name")
    private String auditHospitalName;

    @Schema(description = "审核医生科室")
    @TableField("audit_department_name")
    private String auditDepartmentName;

    @Schema(description = "审核医生姓名")
    @TableField("audit_doctor_name")
    private String auditDoctorName;

    @Schema(description = "审核医生身份证号")
    @TableField("audit_doctor_id_card")
    private String auditDoctorIdCard;

    @Schema(description = "审核医生头像")
    @TableField("audit_doctor_head_img")
    private String auditDoctorHeadImg;

    @Schema(description = "审核医生职称")
    @TableField("audit_doctor_office_holder_name")
    private String auditDoctorOfficeHolderName;

    @Schema(description = "审核结果")
    @TableField("audit_result")
    private String auditResult;

    @Schema(description = "审核时间")
    @TableField("audit_time")
    private String auditTime;

    @Schema(description = "详细意见内容")
    @TableField("audit_opinion")
    private String auditOpinion;

    @Schema(description = "会诊报告主键")
    @TableField("report_id")
    private String reportId;

    @Schema(description = "是否是主治医生 0:不是，1:是")
    @TableField("is_main")
    private String isMain;

    @Schema(description = "会诊医生类型   助理医生 ASSISTANT  会诊医生 CONSULTANT")
    @TableField("doctor_type")
    private String doctorType;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private String createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private String updateTime;
}
