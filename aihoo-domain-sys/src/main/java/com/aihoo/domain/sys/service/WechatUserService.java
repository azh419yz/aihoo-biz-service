package com.aihoo.domain.sys.service;


import com.aihoo.domain.sys.model.entity.WechatUser;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-07-24
 */
public interface WechatUserService extends IService<WechatUser> {

    PageResult<WechatUser> wechatUserList(Map<String,Object> map);

    void isWechatUser(Map<String,Object> map);
}
