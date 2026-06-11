package com.aihoo.domain.consultation.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * MDT
 * </p>
 *
 * @author mcp
 * @since 2020-09-21
 */
@Data
@TableName("t_mdt")
public class Mdt implements Serializable {

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
     * icon图片地址
     */
    private String iconImg;

    /**
     * 疾病编码
     */
    private String code;

    /**
     * 疾病名称
     */
    private String name;

    /**
     * 会诊费
     */
    private String price;

    /**
     * 疾病介绍
     */
    private String mdtSynopsis;

    /**
     * 疾病介绍
     */
    private String content;

    /**
     * 是否启用 0-禁用 1-启用
     */
    private String status;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;

    /**
     * 会诊挂号费
     */
    private String registerPrice;

    /**
     * mdt所属医院
     */
    private String hospital;

    /**
     * 协调人
     */
    private String moderator;

    /**
     * 联系方式
     */
    private String contactWay;


   /* @TableField(exist = false)
    private List<MdtBanner> mdtBanners;*/

    /**
     * 标签
     */
    @TableField(exist = false)
    private List<DMdtTag> dMdtTags;

}