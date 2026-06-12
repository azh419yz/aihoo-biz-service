package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * APP版本
 * </p>
 *
 * @author mcp
 * @since 2020-11-06
 */
@Data
@TableName("d_version")
public class DVersion implements Serializable {

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
     * 类型 PATIENT-患者 DOCKER-医生
     */
    private String type;

    /**
     * 版本号
     */
    private String number;

    /**
     * APP名称
     */
    private String name;

    /**
     * 是否强制更新 1:强制 0:不强制
     */
    private String isForce;

    /**
     * 版本类型 Android:安卓 IOS:IOS
     */
    private String appType;

    /**
     * 下载链接
     */
    private String downloadUrl;

    /**
     * 描述
     */
    private String content;

    /**
     * 是否已发布 1:是 0:否
     */
    private String pushed;

    /**
     * 发布时间
     */
    private String publishTime;
}
