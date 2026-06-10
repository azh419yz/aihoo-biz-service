package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname TBase
 * @Description hf
 * @Date 2020/9/29 17:09
 * @Created by ad
 */
@Data
@TableName("t_base")
public class TBase implements Serializable {
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
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 名称
     */
    private String title;

    /**
     * KEY
     */
    @TableField("`key`")
    private String key;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序字段
     */
    @TableField("`index`")
    private String index;

}
