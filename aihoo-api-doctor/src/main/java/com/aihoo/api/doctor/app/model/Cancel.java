package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 申请注销账户表
 * </p>
 *
 * @author mcp
 * @since 2020-11-06
 */
@Data
@TableName("t_cancel")
public class Cancel implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 类型 PATIENT-患者 DOCKER-医生
     */
    private String type;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 患者id
     */
    private String patientUserId;

    /**
     * 申请理由
     */
    private String remark;

    /**
     * 状态 WAIT等待审核 PASS审核通过 REJECT驳回申请
     */
    private String status;

    /**
     * 操作时间
     */
    private String actionTime;

    /**
     * 操作备注或者驳回理由
     */
    private String actiontRemark;
}
