package com.aihoo.domain.patient.model.mapper;

import com.aihoo.domain.patient.model.entity.WechatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 微信用户 Mapper。
 *
 * Created by xieyc on 2020/8/17.
 */
public interface WechatUserMapper extends BaseMapper<WechatUser> {

    void isWechatUser(WechatUser wechatUser);
}
