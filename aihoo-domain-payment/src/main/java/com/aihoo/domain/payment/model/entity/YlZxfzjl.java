package com.aihoo.domain.payment.model.entity;

import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 在线复诊记录表
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Data
@TableName("TB_YL_ZXFZJL")
public class YlZxfzjl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码    复合主键，医保的医院11位代码
     */
    @ExcelColumn(value = "医疗机构代码",col = 1)
    private String yljgdm;

    /**
     * 复诊流水号  复合主键，互联网医院复诊诊疗系统中产生的唯一复诊编码。
     */
    @ExcelColumn(value = "复诊流水号",col = 2)
    private String fzlsh;

    /**
     * 卫生机构（组织）代码
     */
    @ExcelColumn(value = "卫生机构（组织）代码",col = 3)
    private String wsjgdm;

    /**
     * 复诊申请时间    患者申请复诊时间。格式：YYYYMMDD HH:MM:S
     */
    @ExcelColumn(value = "复诊申请时间",col = 4)
    private String fzsqsj;

    /**
     * 患者签到时间    格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "患者签到时间",col = 5)
    private String hzqdsj;

    /**
     * 复诊接诊时间     医生接受复诊申请时间。格式：YYYYMMDD HH:MM:SS
     */
    @ExcelColumn(value = "复诊接诊时间",col = 6)
    private String fzjzsj;

    /**
     * 诊疗时长   单位为“分钟”
     */
    @ExcelColumn(value = "诊疗时长(分)",col = 7)
    private String zlsc;

    /**
     * 卡号
     */
    @ExcelColumn(value = "卡号",col = 8)
    private String kh;

    /**
     * 卡类型    详见“挂号单信息表”的说明（2）
     */
    @ExcelColumn(value = "卡类型",col = 9)
    private String klx;

    /**
     * 身份证件号码
     */
    @ExcelColumn(value = "身份证件号码",col = 10)
    private String sfzjhm;

    /**
     * 身份证件类别代码
     */
    @ExcelColumn(value = "身份证件类别代码",col = 11)
    private String sfzjlx;

    /**
     * 患者姓名
     */
    @ExcelColumn(value = "患者姓名",col = 12)
    private String hzxm;

    /**
     * 就诊科室编码     该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    @ExcelColumn(value = "就诊科室编码",col = 13)
    private String jzksbm;

    /**
     * 诊断编码（主要诊断)    西医：按照上海市统一编码《疾病分类与代码》规范填写；中医：按国标-95执行。若有多条，填写主要诊断。
     */
    @ExcelColumn(value = "诊断编码（主要诊断)",col = 14)
    private String zdbm;

    /**
     * 诊断编码类型   01、上海市统一编码《疾病分类与代码》；02、国标-95
     */
    @ExcelColumn(value = "诊断编码类型",col = 15)
    private String zdbmlx;

    /**
     * 图文音视频档案号     医院自编码，确保在本院内始终唯一。互联网医院复诊全程的图文音视频资料的留痕
     */
    @ExcelColumn(value = "图文音视频档案号",col = 16)
    private String twyspdah;

    /**
     * 数据原文签名值     Base64编码
     */
    @ExcelColumn(value = "数据原文签名值",col = 17)
    private String sjywqmz;

    /**
     * 医护人员证书序列号     接诊医生的CA证书序列号上海CA签发证书序列号
     */
    @ExcelColumn(value = "医护人员证书序列号",col = 18)
    private String yhryzsxlh;

    /**
     * 可信时间戳    Base64编码
     */
    @ExcelColumn(value = "可信时间戳",col = 19)
    private String kxsjc;

    /**
     * 评价  1、好评2、一般3、差评
     */
    @ExcelColumn(value = "评价",col = 20)
    private String pj;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    @ExcelColumn(value = "修改标志",col = 21)
    private String xgbz;
}