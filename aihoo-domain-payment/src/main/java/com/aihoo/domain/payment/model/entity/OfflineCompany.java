package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 公司表
 *
 * @author lenovo
 */
@Data
@TableName("t_offline_company")
public class OfflineCompany implements Serializable {
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
     * 公司名称
     */
    private String name;

    /**
     * 是否删除 0 否 1是
     */
    private String isDelete;

    private static final long serialVersionUID = 1L;
}