package com.aihoo.domain.visit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_hos_sick_health_records")
public class HosSickHealthRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long hosSickId;
    private Integer idCardVerify;
    private String area;
    private String areaName;
    private String height;
    private String weight;
    private String pastHistory;
    private String allergyHistory;
    private String tongueImages;
    private String faceImages;
    private String medicalRecordImages;
    private String createTime;
    private String updateTime;
}
