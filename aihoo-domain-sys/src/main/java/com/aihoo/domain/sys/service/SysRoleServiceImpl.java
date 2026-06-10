package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysRole;
import com.aihoo.domain.sys.model.mapper.SysRoleMapper;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public PageResult<SysRole> getList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (map.get("page") != null && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (map.get("limit") != null && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        if (map.get("roleName") != null && !"".equals(map.get("roleName"))) {
            wrapper.like("role_name", map.get("roleName").toString());
        }
        wrapper.orderByDesc("created_date");

        IPage<SysRole> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveDto(SysRole role, String operatorId) {
        role.setCreateUser(operatorId);
        return save(role);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateDto(SysRole role) {
        return updateById(role);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delDto(String id) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setDeleted("1");
        return updateById(role);
    }
}