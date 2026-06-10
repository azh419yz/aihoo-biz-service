package com.aihoo.domain.sys.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
    * 科室字典表
    */
@Data
@TableName("TB_DIC_Department")
public class DicDepartment {

    /**
     * 科室id
     */
    private String departmentId;
    /**
    * 医疗机构代码 复合主键，医保的医院11位代码。
    */
    private String yljgdm;

    /**
    * 标准科室分类代码  复合主键，该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
    */
    private String wsjdm;

    /**
     * 卫生机构（组织）代码
     */
    private String wsjgdm;

    /**
     * 停用标志  1、启用；0、停用。
     */
    private String kstybz;

    /**
    * 科室级别   1、一级科室；2、二级科室；3、三级科室。指院内科室级别
    */
    private String ksjb;

    /**
    * 是否开展预约   1、是；0、否。
    */
    private String sfkzyy;

    /**
    * 修改标志   1、正常；2.撤销。
    */
    private String xgbz;


}