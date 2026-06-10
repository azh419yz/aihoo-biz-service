package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 角色权限关联表
 * </p>
 *
 * @author zhangh
 * @since 2019-11-05
 */
@TableName("t_sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer menuId;

    /**
     * 创建时间
     */
    private String createdDate;

    /**
     * 更新时间
     */
    private String updatedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "SysRoleMenu{" +
        ", id=" + id +
        ", roleId=" + roleId +
        ", menuId=" + menuId +
        ", createdDate=" + createdDate +
        ", updatedDate=" + updatedDate +
        "}";
    }
}
