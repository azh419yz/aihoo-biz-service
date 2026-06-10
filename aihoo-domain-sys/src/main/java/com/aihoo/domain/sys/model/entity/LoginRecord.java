package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 
 * </p>
 *
 * @author sunjianbo
 * @since 2019-07-17
 */
@Data
@TableName("t_login_record")
public class LoginRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 设备名
     */
    private String device;

    /**
     * 浏览器类型
     */
    private String browserType;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 登录时间
     */
    private String createdDate;

    /**
     * 更新时间
     */
    private String updatedDate;

    private String deleted;

    private String userName;

    /**
     * 操作说明
     */
    private String remark;
}
