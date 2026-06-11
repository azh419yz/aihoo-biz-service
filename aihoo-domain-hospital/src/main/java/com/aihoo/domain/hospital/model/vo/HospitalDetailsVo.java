package com.aihoo.domain.hospital.model.vo;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

/**
 * @Classname HospitalDetailsVo
 * @Description hf
 * @Date 2020/9/17 11:34
 * @Created by ad
 */
@Data
public class HospitalDetailsVo{
    private String id;

    private String hospitalNo;

    private String createTime;

    private String updateTime;

    private String createUserId;

    private String hosName;

    private String hosMobile;

    private String hosGradeCode;

    private String hosGradeName;

    private String hosLevelCode;

    private String hosLevelName;

    private String hosCateCode;

    private String hosCateName;

    private String hosAttCode;

    private String hosAttName;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    private String province;

    private String city;

    private String district;

    private String address;

    private String status;

    private String imgs;

    private String content;

    private Long doctorCount;

    private JSONArray departCodeArray;
}
