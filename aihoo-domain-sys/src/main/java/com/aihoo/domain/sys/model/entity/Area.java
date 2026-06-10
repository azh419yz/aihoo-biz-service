package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 地区表
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@Data
@TableName("d_area")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型 1-省 2-市 3-区
     */
    private String type;

    /**
     * 编码
     */
    private String areaCode;

    /**
     * 上级编码
     */
    private String parentAreaCode;
}
