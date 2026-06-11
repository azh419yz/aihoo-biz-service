package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname MdtTag
 * @Description hf
 * @Date 2020/12/22 9:30
 * @Created by ad
 */
@Data
@TableName("t_mdt_tag")
public class MdtTag implements Serializable {
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
    private String mdtId;

    /**
     * 标签的id
     */
    private String tagId;

    private static final long serialVersionUID = 1L;
}