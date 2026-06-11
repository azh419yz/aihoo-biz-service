package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * t_mdt_order_doctor
 *
 * @author
 */
@Data
@TableName("t_mdt_order_doctor")
public class MdtOrderDoctor implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * mdt订单id
     */
    private String mdtOrderId;

    /**
     * 审核医生医院
     */
    private String hospitalName;

    /**
     * 审核医生科室
     */
    private String departmentName;

    /**
     * 审核医生身份证号
     */
    private String doctorIdCard;

    /**
     * 审核医生姓名
     */
    private String doctorName;

    /**
     * 审核医生头像
     */
    private String headImg;

    /**
     * 医生职称
     */
    private String officeHolderName;

    /**
     * 审核结果
     */
    private String auditResult;

    /**
     * 审核时间
     */
    private String auditTime;

    /**
     * 详细意见内容
     */
    private String auditOpinion;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 是否是主治医生 0:不是，1:是
     */
    private String isMain;

    /**
     * 会诊医生类型   助理医生 ASSISTANT  会诊医生 CONSULTANT
     */
    private String doctorType;

    /**
     * 是否同意 0-否 1-是
     */
    private String isHave;

    /**
     * 会诊接单时间
     */
    private String planTime;

    /**
     * 驳回原因
     */
    private String rejection;

    /**
     * 会诊医生ID
     */
    private String consultantId;

    /**
     * 时间是否确认 0-否 1-是
     */
    private String isAgree;

    private static final long serialVersionUID = 1L;
}