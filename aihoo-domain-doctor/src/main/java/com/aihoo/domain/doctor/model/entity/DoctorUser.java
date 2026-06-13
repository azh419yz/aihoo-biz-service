package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_doctor_user")
public class DoctorUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    // 现有字段（IM 域依赖 - 保留）
    private String name;
    private String hospitalName;
    private String departName;
    private String teamId;
    private String isMain;
    private String introduction;
    private String isAuth;
    private String status;
    private String isCancel;
    private String isMdt;
    private String mdtName;
    private String teamName;
    private String papersNumbers;
    private String mobile;
    private String headImg;
    private String sex;
    private String birthday;
    private String doctorUserId;
    private String patientUserId;
    private String userName;
    private String userSig;
    private String sdkappid;
    private String privstr;
    private String msgType;
    private String msgContent;

    // 老 admin 新增字段
    private String createTime;
    private String updateTime;
    private String createUserId;
    private String age;
    private String tag;
    private String memberNum;
    private String hospitalId;
    private String departId;
    private String departCode;
    private String profession;
    private String duty;
    private String adept;
    private String introductionImg;
    private String papersType;
    private String isAuthentication;
    private String cancelReason;
    private String account;
    private String password;
    private String userType;
    private String roleId;
    private String pushToken;
    private String sdkAppId;
    private String lastLoginTime;
    private String lastLoginIp;
    private String isDelete;
    private String isIndex;
    private String indexImage;
    private String isOnline;

    @TableField(exist = false)
    private List<Object> doctorSetTimes;
}
