package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
    * 疾病基础表
    */
@Data
@TableName("d_disease")
public class Disease implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
    * 疾病名称
    */
    private String name;

    /**
    * 疾病编码
    */
    private String code;

    /**
    * 是否删除 1:已删除 0:可用
    */
    private String isDelete;
}