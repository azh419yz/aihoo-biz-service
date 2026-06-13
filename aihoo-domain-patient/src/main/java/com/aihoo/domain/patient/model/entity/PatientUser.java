package com.aihoo.domain.patient.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("t_patient_user")
public class PatientUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    // IM 域依赖字段 - 保留
    private String name;
    private String phone;
    private String mobile;
    private String headImg;
    private String sex;
    private String birthday;
    private String papersNumbers;
    private String patientUserId;
    private String doctorUserId;
    private String userName;
    private String userSig;
    private String sdkappid;
    private String privstr;
    private String msgType;
    private String status;
    private String isDispose;

    // 老 admin 新增字段
    private LocalDateTime createTime;
    private String updateTime;
    private String wechatOpenId;
    private String alipayOpenId;
    private String nickName;
    private String idCard;
    private String birthDay;
    private String isAuth;
    private String authTime;
    private String token;
    private String unionId;
    private String appleId;
    private String isCancel;

    @TableField(exist = false)
    private List<Object> addresses;

    @TableField(exist = false)
    private List<Object> hosSicks;
}
