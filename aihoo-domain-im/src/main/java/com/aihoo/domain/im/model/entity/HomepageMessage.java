package com.aihoo.domain.im.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @Classname HomepageMessage
 * @Description hf
 * @Date 2020/9/25 11:01
 * @Created by ad
 */
@Data
@TableName("t_homepage_message")
public class HomepageMessage implements Serializable {
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
     * 创建人id
     */
    private String createUserId;

    /**
     * 类型 PATIENT-患者 DOCKER-医生
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

}
