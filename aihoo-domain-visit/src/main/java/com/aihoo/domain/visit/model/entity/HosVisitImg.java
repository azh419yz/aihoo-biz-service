package com.aihoo.domain.visit.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 在线问诊信息图片表
 * </p>
 *
 */
@Data
@TableName("t_hos_visit_img")
public class HosVisitImg implements Serializable {
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
     * 在线问诊信息id
     */
    private String hosVisitId;

    /**
     * 附件图片
     */
    private String img;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;


}
