package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    int insertRoleAuths(@Param("roleId") Integer roleId, @Param("authIds") List<Integer> authIds);


}
