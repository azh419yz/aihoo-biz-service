package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 互联网医院业务量统计表
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
@Data
@TableName("TB_TJ_HLWYY_YWL")
public class TjHlwyyYwl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构代码   复合主键，医保的医院11位代码。
     */
    private String yljgdm;

    /**
     * 科室编码    复合主键，该科室代码对应“医疗卫生机构业务科室分类与代码”（卫统）标准的代码
     */
    private String ksbm;

    /**
     * 业务时间    复合主键，是指业务发生的日期，格式为YYYYMMDD
     */
    private String ywsj;

    /**
     * 卫生机构（组织）代码
     */
    private String wsjgdm;

    /**
     * 健康档案调阅次数  患者复诊时，医生调阅患者健康档案次数。无该业务数据，填报“0”
     */
    private Integer jkdadycs;

    /**
     * 在线咨询人次   无该业务数据，填报“0”
     */
    private Integer zxzrc;

    /**
     * 在线预约人次   无该业务数据，填报“0”
     */
    private Integer zxyyrc;

    /**
     * 在线挂号人次   无该业务数据，填报“0”
     */
    private Integer zxghrc;

    /**
     * 复诊诊疗人次  无该业务数据，填报“0
     */
    private Integer fzzlrc;

    /**
     * 电子处方张数   无该业务数据，填报“0”
     */
    private Integer dzcfzs;

    /**
     * 检验申请单数    开展此项服务后必填。无该业务数据，填报“0”
     */
    private Integer jysqds;

    /**
     * 检查申请单数   开展此项服务后必填。无该业务数据，填报“0”
     */
    private Integer jcsqds;

    /**
     * 护理服务人次  开展此项服务后必填。无该业务数据，填报“0”
     */
    private Integer hlfwrc;

    /**
     * 修改标志  1、正常；2.撤销
     */
    private String xgbz;
}
