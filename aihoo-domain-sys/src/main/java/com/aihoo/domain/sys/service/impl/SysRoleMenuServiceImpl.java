package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.model.entity.SysRoleMenu;
import com.aihoo.domain.sys.model.mapper.SysRoleMenuMapper;
import com.aihoo.domain.sys.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Override
    public boolean updateRoleAuth(Integer roleId, List<Integer> menuIds) { return true; }
}
