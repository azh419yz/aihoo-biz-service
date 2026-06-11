package com.aihoo.domain.payment.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_offline_blacklist")
public class OfflineBlacklist implements Serializable {
    /**
     * 会诊报告主键ID
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
     * 患者姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 证件号
     */
    private String certificates;

    /**
     * 医院id
     */
    private String hospitalId;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 拉入黑名单时间
     */
    private String realDate;

    /**
     * 状态 0未删除 1已删除
     */
    private String isDelete;

    private static final long serialVersionUID = 1L;
}