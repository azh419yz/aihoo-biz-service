package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 余额加减日志表
 */
@Data
@TableName("t_doctor_balance_log")
public class DoctorBalanceLog implements Serializable {
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
     * 医生id
     */
    private String doctorUserId;

    /**
     * 操作类型  REVISIT_ADD-复诊增加 VISIT_ADD-问诊增加 REVISIT_LOSE-复诊减少 VISIT_LOSE-问诊减少
     */
    private String type;

    /**
     * 变动金额
     */
    private String changeAmount;

    /**
     * 可用余额
     */
    private String availableAmount;

    /**
     * 锁定余额
     */
    private String lockAmount;

    /**
     * 日志
     */
    private String remark;

    /**
     * 关联复诊或问诊id
     */
    private String otherId;

    private static final long serialVersionUID = 1L;
}