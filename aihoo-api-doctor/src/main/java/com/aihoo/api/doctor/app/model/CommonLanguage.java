package com.aihoo.api.doctor.app.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
    * 常用语表
    */
@Data
@TableName("t_common_language")
public class CommonLanguage implements Serializable {
    /**
    * 主键ID
    */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
    * 创建时间
    */
    @JSONField(serialize=false)
    private String createTime;

    /**
    * 更新时间
    */
    @JSONField(serialize=false)
    private String updateTime;

    /**
    * 类型 PATIENT-患者 DOCKER-医生
    */
    @JSONField(serialize=false)
    private String type;

    /**
     * 医生id
     */
    @JSONField(serialize=false)
    private String doctorId;

    /**
    * 常用语
    */
    private String content;

    /**
    * 排序
    */
    @JSONField(serialize=false)
    @TableField(value = "`index`")
    private String index;

    /**
    * 是否删除 1:已删除 0:可用
    */
    @JSONField(serialize=false)
    private String isDelete;
}