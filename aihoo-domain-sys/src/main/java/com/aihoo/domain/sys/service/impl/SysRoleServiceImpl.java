package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.model.entity.SysRole;
import com.aihoo.domain.sys.model.mapper.SysRoleMapper;
import com.aihoo.domain.sys.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override public Object getList(Map<String, Object> map) { return null; }
    @Override public boolean saveDto(Map<String, Object> map) { return true; }
    @Override public boolean updateDto(Map<String, Object> map) { return true; }
    @Override public boolean delDto(Map<String, Object> map) { return true; }
    @Override public boolean updateRoleAuth(Integer roleId, List<Integer> menuIds) { return true; }
}
