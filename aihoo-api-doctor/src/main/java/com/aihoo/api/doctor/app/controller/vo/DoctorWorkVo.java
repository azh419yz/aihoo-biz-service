package com.aihoo.api.doctor.app.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 医生用户表
 * </p>
 *
 * @author mcp
 * @since 2020-09-15
 */
@Data
public class DoctorWorkVo implements Serializable {
    //医生id
    private String id;
    //医生
    private String name;
    //头像
    private String headImg;
    //就职医院
    private String hospitalName;
    //所属科室
    private String departName;
    //职称
    private String officeHolderName;
    //工作状态
    private String workStatus;
    //接待量
    private String orderNumber;
    //好评率
    private String highOpinion;
    //是否开启专家咨询
    private String isImg;
    //是否开启复诊
    private String isRevisit;
    //是否开启会诊
    private String isMdt;
    //是否开启会诊
    private String amount;
}
