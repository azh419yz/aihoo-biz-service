package com.aihoo.domain.visit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/3/6 15:06
 */
@Data
@TableName("t_hos_sick_health_records")
public class HosSickHealthRecords implements Serializable {

    /**
     * 主键自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 患者id(db:hos_sick_id)
     */
    private Long hosSickId;
    /**
     * 是否实名制 (db: id_card_verify)
     */
    private Integer idCardVerify;

    /**
     * 区域 (db: area)
     */
    private String area;

    /**
     * 区域 (db: area_name)
     */
    private String areaName;

    /**
     * 身高 (db: height)
     */
    private String height;

    /**
     * 体重 (db: weight)
     */
    private String weight;

    /**
     * 既往病史 (db: past_history)
     */
    private String pastHistory;

    /**
     * 过敏史 (db: allergy_history)
     */
    private String allergyHistory;
    /**
     * 舌照 (db: tongue_images)
     */
    private String tongueImages;
    /**
     * 面照 (db: face_images)
     */
    private String faceImages;
    /**
     * 病例 (db: medical_record_images)
     */
    private String medicalRecordImages;
    /**
     * 创建时间 (db: create_time)
     */
    private String createTime;

    /**
     * 更新时间 (db: update_time)
     */
    private String updateTime;
}
