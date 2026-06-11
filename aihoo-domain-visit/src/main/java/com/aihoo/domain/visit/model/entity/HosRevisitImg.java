package com.aihoo.domain.visit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname HosRevisitImg
 * @Description hf
 * @Date 2020/9/22 20:13
 * @Created by ad
 */
@Data
@TableName("t_hos_revisit_img")
public class HosRevisitImg implements Serializable {
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
    private String hosRevisitId;

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
