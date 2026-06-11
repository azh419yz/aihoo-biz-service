package com.aihoo.domain.patient.service;


import com.aihoo.common.PageResult;
import com.aihoo.domain.patient.model.entity.WechatUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 微信用户服务类
 * </p>
 *
 * @author mcp
 * @since 2020-07-24
 */
public interface WechatUserService extends IService<WechatUser> {

    PageResult<WechatUser> wechatUserList(Map<String,Object> map);

    void isWechatUser(Map<String,Object> map);
}
