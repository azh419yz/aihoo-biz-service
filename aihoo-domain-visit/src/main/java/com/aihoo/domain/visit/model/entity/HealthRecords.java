package com.aihoo.domain.visit.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
    * 健康档案调阅
    */
@Data
@TableName("t_health_records")
public class HealthRecords implements Serializable {
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
    * 科室code
    */
    private String deptCode;

    /**
    * 复诊id
    */
    private String revisitId;

    private static final long serialVersionUID = 1L;
}