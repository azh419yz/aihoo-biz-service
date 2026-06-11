package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname DoctorAync
 * @Description hf
 * @Date 2020/9/28 13:55
 * @Created by ad
 */
@Data
@TableName("t_doctor_aync")
public class DoctorAync implements Serializable {

    private static final long serialVersionUID = 1L;
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
     * 展示好评率 90%值为0.90
     */
    private String highOpinion;

    /**
     * 真实好评率 90%值为0.90
     */
    private String realHighOpinion;
}
