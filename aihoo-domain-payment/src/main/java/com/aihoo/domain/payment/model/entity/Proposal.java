package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname Proposal
 * @Description hf
 * @Date 2020/12/3 13:24
 * @Created by ad
 */
@Data
@TableName("t_proposal")
public class Proposal implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.AUTO)
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
     * 患者用户ID
     */
    private String patientUserId;

    /**
     * 医生ID
     */
    private String doctorUserId;

    /**
     * 意见内容
     */
    private String content;

    /**
     * 是否处理该问题（0 未处理 1 已经处理）
     */
    private String isDispose;

    /**
     * 对应人的手机号
     */
    @TableField(exist = false)
    private String mobile;

    private static final long serialVersionUID = 1L;
}