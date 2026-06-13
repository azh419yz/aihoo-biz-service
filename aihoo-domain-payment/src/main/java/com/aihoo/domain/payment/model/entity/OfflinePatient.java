package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 线下就诊人
 */
@Data
@TableName("t_offline_patient")
public class OfflinePatient implements Serializable {
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
     * 就诊人名称
     */
    private String name;

    /**
     * 就诊人手机号
     */
    private String mobile;

    /**
     * 证件类型编码  t_dict字典表 type=PAPERS
     */
    private String papersCode;

    /**
     * 证件类型名称
     */
    private String papersName;

    /**
     * 证件号码
     */
    private String papersNumbers;

    /**
     * 状态 0正常 1黑名单
     */
    private String status;

    /**
     * 状态 0未删除 1已删除
     */
    private String isDelete;

    private static final long serialVersionUID = 1L;
}