package com.aihoo.api.doctor.app.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：lsl
 * @date ：Created in 2020/9/21 18:42
 * @description：首页提示信息表
 */
@TableName("t_homepage_message")
@Data
public class HomePageMessage implements Serializable {
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
