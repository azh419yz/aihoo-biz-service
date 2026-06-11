package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 处方用法用量表
 */
@Data
@TableName("t_hos_prescription_instruction")
public class HosPrescriptionInstruction implements Serializable {
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
     * 处方id
     */
    private String hosPrescriptionId;

    /**
     * 用法 1:内服 2:外用
     */
    @TableField("`usage`")
    private String usage;

    /**
     * 全部剂数
     */
    private String doseNumber;

    /**
     * 每日剂量
     */
    private String dose;

    /**
     * 每剂使用次数
     */
    private String times;

    /**
     * 煎药方式 1:待煎 2:自煎
     */
    private String decoctionMethod;

    /**
     * 煎药规格 1:100ml/袋 2:200ml/袋
     */
    private String decoctionSize;

    /**
     * 医嘱
     */
    private String advice;

    /**
     * 备注
     */
    private String remark;
}
