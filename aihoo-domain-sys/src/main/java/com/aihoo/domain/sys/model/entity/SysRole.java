package com.aihoo.domain.sys.model.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * <p>
 * 角色表
 * </p>
 *
 * @author zhangh
 * @since 2019-11-05
 */
@Data
@TableName("t_sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String comments;

    /**
     * 是否删除，0否，1是
     */
    private String deleted;

    /**
     * 创建时间
     */
    private String createdDate;

    /**
     * 修改时间
     */
    private String updatedDate;

    /**
     * 创建人
     */
    private String createUser;
}
