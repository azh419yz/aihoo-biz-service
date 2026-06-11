package com.aihoo.domain.patient.service.impl;



import com.aihoo.common.PageResult;
import com.aihoo.domain.patient.model.entity.WechatUser;
import com.aihoo.domain.patient.model.mapper.WechatUserMapper;
import com.aihoo.domain.patient.service.WechatUserService;
import com.aihoo.redis.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

/**
 * <p>
 * 微信用户服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-07-24
 */
@Service("wechatUserService")
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper, WechatUser> implements WechatUserService {

    @Autowired
    private WechatUserMapper wechatUserMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public PageResult<WechatUser> wechatUserList(Map<String, Object> map) {

        System.out.println(map);
        //首页默认page为0,limit为10
        Integer page = null != map.get("page") && !"".equals(map.get("page")) ? Integer.parseInt(map.get("page").toString()) : 0;
        Integer limit = null != map.get("limit") && !"".equals(map.get("limit")) ? Integer.parseInt(map.get("limit").toString()) : 10;

        //定义页码和每页显示条数
        Page<WechatUser> pageWechatUser = new Page<>();
        pageWechatUser.setCurrent(page);
        pageWechatUser.setTotal(limit);
        QueryWrapper<WechatUser> qw = new QueryWrapper<>();
        //查询所需要的字段
        qw.select("id","create_time","update_time","nick_name","head_img","mobile","state");
        //按照更新时间 倒叙
        qw.orderByDesc("update_time");

        //查询数据
        IPage<WechatUser> wechatUserIPage = wechatUserMapper.selectPage(pageWechatUser, qw);


        return new PageResult<>(wechatUserIPage.getRecords(), wechatUserIPage.getTotal());
    }

    @Override
    public void isWechatUser(Map<String, Object> map) {
/*
        String startTime = null != map.get("startTime") && !"".equals(map.get("startTime")) ? map.get("startTime").toString() : null;
        String endTime = null != map.get("endTime") && !"".equals(map.get("endTime")) ? map.get("endTime").toString(): null;

        WechatUser wechatUser=new WechatUser();
        if ("0".equals(map.get("state").toString())){
            wechatUser.setState(false);
            wechatUser.setLogin(false);
            WechatUser wechatUserDB = wechatUserMapper.selectById(map.get("id").toString());

            boolean exists = redisService.exists(RedisConstant.USER_LOGIN_KEY + wechatUserDB.getOpenId());

            System.out.println(wechatUserDB.getOpenId());
            System.out.println(exists);
            if (exists){
                redisService.remove(RedisConstant.USER_LOGIN_KEY + wechatUserDB.getOpenId());
            }


            wechatUser.setStartTime(startTime);
            wechatUser.setStartTime(endTime);
        }else {
            wechatUser.setState(true);
        }

        wechatUser.setId(map.get("id").toString());



        //根据id 修改用户信息
        wechatUserMapper.isWechatUser(wechatUser);*/
    }


}
