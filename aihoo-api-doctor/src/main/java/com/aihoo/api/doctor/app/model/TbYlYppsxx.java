package com.aihoo.api.doctor.app.model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 药品配送信息表
 * </p>
 *
 * @author mcp
 * @since 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbYlYppsxx implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 医疗机构代码  复合主键，医保的医院11位代码
     */
    private String yljgdm;

    /**
     * 处方号    复合主键
     */
    private String cyh;

    /**
     * 复诊流水号    互联网医院复诊诊疗系统中产生的唯一复诊编码
     */
    private String fzlsh;

    /**
     * 卫生机构（组织）代码
     */
    private String wsjgdm;

    /**
     * 配送方式  0、医院取药；1、物流配送；2、药店取药。
     */
    private String psfs;

    /**
     * 配送开始时间  格式：YYYYMMDD HH:MM:SS
     */
    private LocalDateTime pskssj;

    /**
     * 配送结束时间    格式：YYYYMMDD HH:MM:SS
     */
    private LocalDateTime psjssj;

    /**
     * 配送状态  1、完成；0、未完成。
     */
    private String pszt;

    /**
     * 修改标志  1、正常；2.撤销。
     */
    private String xgbz;

}
