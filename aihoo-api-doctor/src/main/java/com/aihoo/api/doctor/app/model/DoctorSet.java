package com.aihoo.api.doctor.app.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 接诊设置表
 */
@Data
@TableName("t_doctor_set")
public class DoctorSet implements Serializable {
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
     * 是否开启专家咨询 0-未开 1-开启
     */
    private String isImg;

    /**
     * 是否开启语音问诊 0-未开 1-开启[废弃]
     */
    private String isVoice;

    /**
     * 是否开启视频问诊 0-未开 1-开启[废弃]
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
     * 语音问诊价格[废弃]
     */
    private String voicePrice;

    /**
     * 视频问诊价格[废弃]
     */
    private String videoPrice;

    /**
     * 是否开启会诊 0-未开 1-开启
     */
    private String isMdt;

    private static final long serialVersionUID = 1L;
}