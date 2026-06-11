package com.aihoo.domain.visit.model.vo;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 就诊人信息表
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Data
@TableName("t_hos_sick")
public class HosSickVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 手机号
     */
    private String mobile;


    @TableField(exist = false)
    private String patientUser;

    /**
     * 患者用户id
     */
    private String patientUserId;
}
