package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleMenuService extends IService<SysRoleMenu> {
    boolean updateRoleAuth(Integer roleId, List<Integer> menuIds);
}
