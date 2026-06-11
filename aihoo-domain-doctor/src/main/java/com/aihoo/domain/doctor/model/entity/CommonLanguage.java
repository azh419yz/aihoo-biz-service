package com.aihoo.domain.doctor.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @Classname CommonLanguage
 * @Description hf
 * @Date 2020/10/29 14:38
 * @Created by ad
 */
@Data
@TableName("t_common_language")
public class CommonLanguage {
    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.AUTO)
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
     * 类型 PATIENT-患者 DOCKER-医生
     */
    private String type;

    /**
     * 医生id
     */
    private String doctorId;

    /**
     * 常用语
     */
    private String content;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

    private static final long serialVersionUID = 1L;
}
