package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_mdt_order_doctor")
public class MdtOrderDoctor implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String mdtOrderId;
    private String hospitalName;
    private String departmentName;
    private String doctorIdCard;
    private String doctorName;
    private String headImg;
    private String officeHolderName;
    private String auditResult;
    private String auditTime;
    private String auditOpinion;
    private String doctorUserId;
    private String isMain;
    private String doctorType;
    private String isHave;
    private String planTime;
    private String rejection;
    private String consultantId;
    private String isAgree;
    private static final long serialVersionUID = 1L;
}
