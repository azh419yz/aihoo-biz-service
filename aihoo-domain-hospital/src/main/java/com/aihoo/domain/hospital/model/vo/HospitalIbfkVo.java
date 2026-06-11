package com.aihoo.domain.hospital.model.vo;

import lombok.Data;

/**
 * @ClassName: HospitalIbfkVo .class
 * @author: gongjiankang
 * @create: 2021-03-26 10:12
 **/
@Data
public class HospitalIbfkVo {

    /**
     * 医院id
     */
    private String hospitalNo;

    /**
     * 医院名称
     */
    private String hosName;
}
