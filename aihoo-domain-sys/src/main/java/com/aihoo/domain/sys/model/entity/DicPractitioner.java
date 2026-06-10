package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 医务人员字典表
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
@Data
@TableName("TB_DIC_Practitioner")
public class DicPractitioner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医生表ID
     */
    private String doctorUserId;
    /**
     * 医疗机构代码  复合主键，医保的医院11位代码
     */
    private String yljgdm;

    /**
     * 工号  复合主键，保证医护人员工号院内唯一
     */
    private String gh;

    /**
     * 所属科室   该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    private String ssks;

    /**
     * 卫生机构（组织）代码
     */
    private String wsjgdm;

    /**
     * 姓名    医护人员姓名
     */
    private String xm;

    /**
     * 证件类型   01、居民身份证；02、资格证书；首选身份证，若无居民身份证，填写02：资格证书
     */
    private String zjlx;

    /**
     * 证件号码   首选身份证号，若无则填写资格证书编号。
     */
    private String zjhm;

    /**
     * 职务代码  按国标GB/T12407-2008职务级别代码执行编码。
     */
    private String zwdm;

    /**
     * 职称代码   按国标GB/T 8561-2001 专业技术职务代码执行
     */
    private String zcdm;

    /**
     * 人员类别  即从事专业类别代码。类别代码为“31西药师（士）”或“32中药师(士)”时必填。参考《上海市卫生资源与医疗服务调查制度》，详见说明（1）。
     */
    private String lb;

    /**
     * CA证书序列号  除人员类别为“31西药师（士）”或“32中药师(士)”可不填写，其他人员类别必填。
     */
    private String cazsxlm;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    private String xgbz;
}
