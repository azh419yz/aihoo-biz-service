package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname DMdtTag
 * @Description hf
 * @Date 2020/12/22 9:45
 * @Created by ad
 */
@Data
@TableName("d_mdt_tag")
public class DMdtTag implements Serializable {
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
     * 创建人id
     */
    private String createUserId;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;

    /**
     * 是否删除  0 否 1 是
     */
    private String isDelete;

    private static final long serialVersionUID = 1L;
}