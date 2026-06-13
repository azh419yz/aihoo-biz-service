package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author dumingming
 * @since 2019-12-13
 */
@TableName("t_sys_user")
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * Im通讯密码
     */
    private String userSig;
    private String password;

    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱是否验证,0未验证,1已验证
     */
    private String emailVerified;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 状态，0正常，1冻结
     */
    private String status;

    /**
     * 注册时间
     */
    private String createdDate;

    /**
     * 修改时间
     */
    private String updatedDate;

    private String deleted;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 登陆失败次数
     */
    private String errorCount;

    /**
     * 账号锁定时间
     */
    private String userLockTime;

    /**
     * 账号密码修改时间
     */
    private String passwordUpdate;


    private Integer permission;
}
