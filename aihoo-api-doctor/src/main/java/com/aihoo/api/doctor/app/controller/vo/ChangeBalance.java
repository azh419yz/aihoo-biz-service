package com.aihoo.api.doctor.app.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 余额变更记录
 * @author: Mr.Li
 * @create: 2020-09-29 09:32
 **/
@Data
public class ChangeBalance implements Serializable {
    private String creatTime;//创建时间
    private String type;//操作类型  ADD-增加 LOSE-减少
    private String changeAmount;//变动金额
    private String remark;//操作日志
}
