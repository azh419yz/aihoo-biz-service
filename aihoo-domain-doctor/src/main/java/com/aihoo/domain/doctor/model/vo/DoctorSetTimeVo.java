package com.aihoo.domain.doctor.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @Classname DoctorSetTimeVo
 * @Description hf
 * @Date 2020/9/21 19:10
 * @Created by ad
 */
@Data
public class DoctorSetTimeVo {
    private String weekCode;
    private List<SetTime> setTimes;

    @Data
    public static class SetTime {
        private String startTime;
        private String endTime;
    }

}
