package com.aihoo.api.doctor.app.model;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/22 8:40
 * @description：字典
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础设置表
 */
@TableName("d_dict")
@Data
public class Dict implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 类型 医院等级HOS_LEVEL
     */
    private String type;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private String index;

}