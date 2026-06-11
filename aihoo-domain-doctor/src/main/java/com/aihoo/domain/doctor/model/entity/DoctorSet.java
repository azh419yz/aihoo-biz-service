package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname DoctorSet
 * @Description hf
 * @Date 2020/9/19 16:22
 * @Created by ad
 */
@Data
@TableName("t_doctor_set")
public class DoctorSet implements Serializable {
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
     * 是否开启图文问诊 0-未开 1-开启
     */
    private String isImg;

    /**
     * 是否开启语音问诊 0-未开 1-开启
     */
    private String isVoice;

    /**
     * 是否开启视频问诊 0-未开 1-开启
     */
    private String isVideo;

    /**
     * 是否开启复诊 0-未开 1-开启
     */
    private String isRevisit;

    /**
     * 图文问诊价格
     */
    private String imgPrice;

    /**
     * 语音问诊价格
     */
    private String voicePrice;

    /**
     * 视频问诊价格
     */
    private String videoPrice;

    /**
     * 是否开启会诊 0-未开 1-开启'
     */
    private String isMdt;

    /**
     * 是否开启组合医生 0-未开 1-开启
     */
    private String isCombination;

}
