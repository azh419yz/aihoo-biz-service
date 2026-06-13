package com.aihoo.domain.payment.model.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 医院表 （目前医院 华山、华东）
 **/
@Data
@TableName("t_offline_card")
public class OfflineHospital implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 医院id
     */
    private String hospitalId;
    /**
     * 医院名称
     */
    private String hospitalName;
    /**
     * isDelete
     */
    private String isDelete;
}