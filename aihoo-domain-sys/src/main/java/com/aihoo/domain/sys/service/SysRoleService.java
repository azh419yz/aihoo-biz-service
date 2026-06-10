package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysRole;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    PageResult<SysRole> getList(Map<String, Object> map);

    boolean saveDto(SysRole role, String operatorId);

    boolean updateDto(SysRole role);

    boolean delDto(String id);
}