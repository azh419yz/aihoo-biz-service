package com.aihoo.domain.sys.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname SearchKeywords
 * @Description hf
 * @Date 2020/9/29 17:56
 * @Created by ad
 */
@Data
@TableName("t_search_keywords")
public class SearchKeywords implements Serializable {

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
     * 类型 HOT-热门搜索 VOICE-语音搜索
     */
    private String type;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

    private static final long serialVersionUID = 1L;
}
