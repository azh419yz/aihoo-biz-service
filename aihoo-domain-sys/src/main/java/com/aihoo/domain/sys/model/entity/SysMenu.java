package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 权限表
 * </p>
 *
 * @author zhangh
 * @since 2019-11-05
 */
@TableName("t_sys_menu")
@Data
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 权限名称
     */
    private String menuName;

    /**
     * 授权标识
     */
    private String permission;

    /**
     * 菜单url
     */
    private String menuUrl;

    /**
     * 父id,-1表示无父级
     */
    private String parentId;

    /**
     * 权限类型,0菜单,1按钮
     */
    private String isMenu;

    /**
     * 排序号
     */
    private String orderNumber;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 创建时间
     */
    private String createdDate;

    /**
     * 修改时间
     */
    private String updatedDate;

    private String deleted;


}
