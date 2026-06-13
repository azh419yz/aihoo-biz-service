package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * banner表
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@Data
@TableName("t_banner")
public class Banner implements Serializable {

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
     * 创建人id
     */
    private String type;
    /**
     * 创建人id
     */
    private String otherId;

    /**
     * 标题
     */
    private String title;

    /**
     * banner图
     */
    private String img;

    /**
     * 富文本内容
     */
    private String content;

    /**
     * 排序
     */
    @TableField("`index`")
    private String index;

    /**
     * 是否删除 1:已删除 0:可用
     */
    private String isDelete;

    /**
     * banner类型（IMAGE-图片; VIDEO-视频；）
     */
    private String bannerType;

    /**
     * 视频地址
     */
    private String videoUrl;

}
