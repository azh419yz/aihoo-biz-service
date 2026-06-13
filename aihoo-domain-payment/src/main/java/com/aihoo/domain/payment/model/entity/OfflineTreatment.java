package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 就诊项目表
 **/
@Data
@TableName("t_offline_treatment")
public class OfflineTreatment {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * 项目名称
     */
    private String treatmentName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 是否删除 0否1是
     */
    private String isDelete;
}
