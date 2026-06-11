package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Classname MdtTeam
 * @Description hf
 * @Date 2020/12/22 9:38
 * @Created by ad
 */
@Data
@TableName("t_mdt_team")
public class MdtTeam implements Serializable {
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
     * 团队名称
     */
    private String name;

    /**
     * 团队首页图
     */
    private String homeImg;

    /**
     * 团队详情图
     */
    private String detailsImg;

    /**
     * 团队列表图
     */
    private String listImg;

    /**
     * 团队简介
     */
    private String introduction;

    /**
     * 会诊费
     */
    private String price;

    /**
     * 是否删除 0 否 1 是
     */
    private String isDelete;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;


    /**
     * 团队类型
     * PERSONAL-个人
     * TEAM-团队
     * COMBINATION-组合
     */
    private String mdtType;

    private static final long serialVersionUID = 1L;
}
