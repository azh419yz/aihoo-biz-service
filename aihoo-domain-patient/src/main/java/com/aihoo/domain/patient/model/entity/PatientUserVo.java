package com.aihoo.domain.patient.model.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 患者用户表（分页查询 VO）
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Data
@TableName("t_patient_user")
public class PatientUserVo implements Serializable {

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
     * 手机号
     */
    private String mobile;

    /**
     * 状态(是否启用 1:启用 0:停用)
     */
    private String status;


}
