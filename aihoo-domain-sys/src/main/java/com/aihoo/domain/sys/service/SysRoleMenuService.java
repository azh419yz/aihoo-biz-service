package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    boolean updateRoleAuth(Integer roleId, List<Integer> authIds);
}
