package com.aihoo.api.doctor.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/22 11:27
 * @description：待处理订单
 */
@Data
public class HosOrderList implements Serializable {
    private String id;
    private String name;
    private String sex;
    private String age;
    private String type;
    private String typeName;
//    private Integer count;
}
