package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.WechatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Created by xieyc on 2020/8/17.
 */
public interface WechatUserMapper extends BaseMapper<WechatUser> {

    void isWechatUser(WechatUser wechatUser);
}
