package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    Object getList(Map<String, Object> map);
    boolean saveDto(Map<String, Object> map);
    boolean updateDto(Map<String, Object> map);
    boolean delDto(Map<String, Object> map);
    boolean updateRoleAuth(Integer roleId, List<Integer> menuIds);
}
