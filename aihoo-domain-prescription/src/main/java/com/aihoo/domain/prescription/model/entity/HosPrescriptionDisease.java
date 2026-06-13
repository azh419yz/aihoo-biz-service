package com.aihoo.domain.prescription.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname HosPrescriptionDisease
 * @Description hf
 * @Date 2020/9/24 9:52
 * @Created by ad
 */
@Data
@TableName("t_hos_prescription_disease")
public class HosPrescriptionDisease implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.AUTO)
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
     * 疾病编码 d_disease 表code
     */
    private String diseaseCode;

    /**
     * 疾病名称
     */
    private String diseaseName;
}
