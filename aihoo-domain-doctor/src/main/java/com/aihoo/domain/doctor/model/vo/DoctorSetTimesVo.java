package com.aihoo.domain.doctor.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Classname DoctorSetTimesVo
 * @Description hf
 * @Date 2020/9/22 14:29
 * @Created by ad
 */
@Data
public class DoctorSetTimesVo {
    /**
     * 星期 d_dict type=WEEK
     */
    private String weekCode;

    /**
     * 星期
     */
    private String weekName;

    /**
     * 开始时间 整点 例：01:00
     */
    private String startTime;

    /**
     * 结束时间 整点 例：13:00 结束时间大于开始时间
     */
    private String endTime;

    /**
     * 类型 VOICE VIDEO REVISIT
     */
    private String type;



}
