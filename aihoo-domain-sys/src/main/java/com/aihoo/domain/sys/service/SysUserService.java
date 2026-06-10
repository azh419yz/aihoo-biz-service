package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    PageResult<SysUser> listUser(Map<String, Object> params);

    boolean addUser(SysUser user, String operatorId);

    boolean update(SysUser user);

    boolean updateStatus(String userId, String status);

    boolean resetPsw(String userId, String encryptedPassword);

    boolean isDelete(String id);
}