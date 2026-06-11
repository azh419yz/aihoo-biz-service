package com.aihoo.domain.patient.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
/**
 * <p>
 * 患者收货地址表
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Data
@TableName("t_address")
public class Address implements Serializable {

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
     * 患者用户id
     */
    private String patientUserId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 省 code
     */
    private String provinceCode;

    /**
     * 市 code
     */
    private String cityCode;

    /**
     * 区 code
     */
    private String districtCode;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详情地址
     */
    private String address;

    /**
     * 是否默认地址 1:是 0:不是
     */
    private String isDefault;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;
}
