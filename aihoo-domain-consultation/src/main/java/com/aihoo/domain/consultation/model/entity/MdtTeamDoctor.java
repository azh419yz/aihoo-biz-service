package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @Classname MdtTeamDoctor
 * @Description hf
 * @Date 2020/12/22 9:42
 * @Created by ad
 */
@Data
@TableName("t_mdt_team_doctor")
public class MdtTeamDoctor implements Serializable {
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
     * 更新时间
     */
    private String updateTime;

    /**
     * MDT的id
     */
    private String mdtTeamId;

    /**
     * 医生id
     */
    private String doctorUserId;

    /**
     * 会诊费
     */
    private String price;

    /**
     * 是否是主治医生 0:不是，1:是
     */
    private String isMain;

    /**
     * 会诊医生类型   助理医生 ASSISTANT  会诊医生 CONSULTANT
     */
    private String doctorType;

    /**
     * 组合团队，会诊医生和助理医生关联使用
     */
    private String consultantId;

    @TableField(exist = false)
    private String doctorUserName;

    private static final long serialVersionUID = 1L;
}