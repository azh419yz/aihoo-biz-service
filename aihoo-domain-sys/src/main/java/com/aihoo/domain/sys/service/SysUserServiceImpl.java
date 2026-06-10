package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public PageResult<SysUser> listUser(Map<String, Object> params) {
        long page = 1;
        long limit = 10;

        if (params.get("page") != null && !"".equals(params.get("page"))) {
            page = Long.parseLong(params.get("page").toString());
        }
        if (params.get("limit") != null && !"".equals(params.get("limit"))) {
            limit = Long.parseLong(params.get("limit").toString());
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(SysUser::getTrueName, params.get("trueName"))
                .eq(params.get("phone") != null, SysUser::getPhone, params.get("phone"))
                .orderByDesc(SysUser::getCreatedDate);

        IPage<SysUser> iPage = baseMapper.selectPage(new Page<>(page, limit), queryWrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public boolean addUser(SysUser user, String operatorId) {
        user.setCreateUser(operatorId);
        user.setPasswordUpdate(LocalDateTime.now().toString());
        return save(user);
    }

    @Override
    public boolean update(SysUser user) {
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public boolean updateStatus(String userId, String status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public boolean resetPsw(String userId, String encryptedPassword) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(encryptedPassword);
        user.setPasswordUpdate(LocalDateTime.now().toString());
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public boolean isDelete(String id) {
        return removeById(id);
    }
}