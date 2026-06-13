package com.aihoo.api.admin.controller.vo;

import lombok.Data;

/**
 * @Classname CustomerVo
 * @Description hf
 * @Date 2020/11/10 15:29
 * @Created by ad
 */
@Data
public class CustomerVo {
    // 患者账号
    private String patientPhone;
    //开始时间
    private String startTime;
    // 结束时间
    private String endTime;
    //开始时间
    private String patientId;
    // 结束时间
    private String adminId;
  /*  //聊天详情
    private List<ImCustomerMsg> imCustomerMsgs;*/
}
