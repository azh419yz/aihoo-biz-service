package com.aihoo.domain.consultation.model.excel;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname HospitalEntity
 * @Description hf
 * @Date 2020/10/12 10:15
 * @Created by ad
 */
@Data
public class MdtEntity implements Serializable {

    /**
     * 主键ID
     */
  /*  @ExcelColumn(value = "主键id",col = 1)
    private String id;*/


    /**
     * 创建时间
     */
/*    @ExcelColumn(value = "创建时间",col = 2)
    private String createTime;*/

    /**
     * 更新时间
     */
 /*   @ExcelColumn(value = "主键id",col = 3)
    private String updateTime;*/

    /**
     * 创建人id
     */
   /* @ExcelColumn(value = "创建人id",col = 4)
    private String createUserId;*/

    /**
     * icon图片地址
     */
   /* @ExcelColumn(value = "主键id",col = 5)
    private String iconImg;*/

    /**
     * 疾病编码
     */
    /*@ExcelColumn(value = "疾病编码",col = 6)
    private String code;*/

    /**
     * 疾病名称
     */
    @ExcelColumn(value = "疾病名称",col = 7)
    private String name;

    /**
     * 排序
     */
    @ExcelColumn(value = "排序",col = 8)
    @TableField("`index`")
    private String index;

    /**
     * 会诊挂号费
     */
/*    @ExcelColumn(value = "会诊挂号费",col = 9)
    private String registerPrice;*/


    /**
     * 会诊费
     */
    @ExcelColumn(value = "会诊费",col = 10)
    private String price;

    /**
     * mdt所属医院
     */
    @ExcelColumn(value = "所属医院",col = 11)
    private String hospital;

    /**
     * 协调人
     */
    @ExcelColumn(value = "协调人",col = 12)
    private String moderator;


    /**
     * 联系方式
     */
    @ExcelColumn(value = "联系方式",col = 13)
    private String contactWay;

    /**
     * 是否启用 0-禁用 1-启用
     */
    @ExcelColumn(value = "是否启用",col = 14)
    private String status;

    /**
     * 疾病介绍
     */
  /*  @ExcelColumn(value = "主键id",col = 9)
    private String content;
*/


    /**
     * 是否删除 1:已删除 0:可用
     */
 /*   @ExcelColumn(value = "主键id",col = 11)
    private String isDelete;*/









}