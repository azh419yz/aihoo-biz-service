package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 20:36
 * @description：接诊设置表
 */
@TableName("t_doctor_aync")
@Data
public class DoctorAync implements Serializable {
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
     * 接单量
     */
    private String orderNumber;

    /**
     * 好评率 90%值为0.90
     */
    private String highOpinion;

    /**
     * 真实好评率 90%值为0.90
     */
    private String realHighOpinion;

}