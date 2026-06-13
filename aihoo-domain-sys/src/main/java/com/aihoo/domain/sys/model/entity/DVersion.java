package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * APP版本
 */
@Data
@TableName("d_version")
public class DVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String createTime;
    private String updateTime;
    private String type;
    private String number;
    private String name;
    private String isForce;
    private String appType;
    private String downloadUrl;
    private String content;
    private String pushed;
    private String publishTime;
}
