package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 医院字典表
 */
@Data
@TableName("TB_DIC_Hospital")
public class DicHospital {

    /**
     * 医疗机构代码，复合主键 ，医保的医院 11位代码
     */
    private String yljgdm;

    /**
     * 卫生机构（组织）代码  由22位数字（或英文字母）组成，包括9位组织机构代码和13位机构属性代码。机构属性代码由行政区划代码（6位）、经济类型代码（2位）、卫生机构（组织）类别代码（4位）和机构分类管理代码（1位）四部分组成。
     */
    private String wsjgdm;

    /**
     * 医院名称
     */
    private String yymc;

    /**
     * 医疗机构级别 0、社区卫生服务中心 1、一级 2、二级 3、三级 9、未定级  参考《上海市卫生资源与医疗服务调查制度》
     */
    private String yljgjb;

    /**
     * 医疗机构等级 非社区卫生服务中心则必填。 1、甲等 2、乙等 3、丙等 9、未定等   参考《上海市卫生资源与医疗服务调查制度》
     */
    private String yljgdj;

    /**
     * 医疗机构类别 编码。 按照 “卫生机构（组织）分类与代码（WS218-2002）”编码执行
     */
    private String yljglb;

    /**
     * 自费方式   就医费用中自费部分的支付方式。第1位表示支付宝，第2位表示微信，第3位表示银联，支持的支付方式，在对应占位符上填1，不支持则填0，例如仅支持支付宝、银联支付，填报值为'101'；
     */
    private String zffs;

    /**
     * 医疗机构属性 1、市属 2、区属 3、社会办医疗机构
     */
    private String yljgsx;

    /**
     * 所属区代码    以医疗机构注册地址的行政区划代码为准
     */
    private String ssqdm;

    /**
     * 复诊判断依据 1、诊断代码 2、科室代码 3、诊断代码与科室代码
     */
    private String fzpdyj;

    /**
     * 复诊判断数据来源   1、院内就诊数据 2、市级健康档案 3、医联健康档案 4、区级健康档案
     */
    private String fzpdsjly;

    /**
     * 复诊判断时效  单位为月
     */
    private Integer fzpdsx;

    /**
     * 修改标志  1、正常 2、撤销
     */
    private String xgbz;
}