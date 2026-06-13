package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 就诊项目详情表
 **/
@Data
@TableName("t_offline_treatment_details")
public class OfflineDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * 项目id
     */
    private String treatmentId;
    /**
     * 项目名称
     */
    private String name;
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
