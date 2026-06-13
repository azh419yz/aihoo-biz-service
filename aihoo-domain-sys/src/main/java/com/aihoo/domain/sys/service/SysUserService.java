package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.dto.LoginRequest;
import com.aihoo.domain.sys.model.dto.PhoneLoginRequest;
import com.aihoo.domain.sys.model.dto.SaveUpdateUserRequest;
import com.aihoo.domain.sys.model.dto.SearchUserRequest;
import com.aihoo.domain.sys.model.dto.SendPhoneCodeRequest;
import com.aihoo.domain.sys.model.vo.LoginVo;
import com.aihoo.domain.sys.model.vo.SysUserVo;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    PageResult<SysUserVo> listUser(PageParam<SysUser> pageParam, SearchUserRequest request);

    boolean addUser(SaveUpdateUserRequest request);

    boolean update(SaveUpdateUserRequest request);

    boolean updateStatus(Map<String,Object> map);

    boolean resetPsw(Map<String,Object> map);

    boolean updatePsw(Map<String,Object> map);

    boolean isDelete(String id);

    void updateErrorCount(Map<String,Object> map);

    LoginVo doLogin(LoginRequest request, HttpServletRequest httpRequest);

    void sendPhoneCode(SendPhoneCodeRequest request);

    LoginVo phoneLogin(PhoneLoginRequest request, HttpServletRequest httpRequest);


}
