package com.aihoo.domain.doctor.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoctorUserServiceImpl extends ServiceImpl<DoctorUserMapper, DoctorUser> implements DoctorUserService {

    @Override
    public List<DoctorUser> findDoctorUserAll() {
        QueryWrapper<DoctorUser> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("is_cancel", 0);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public PageResult<DoctorUser> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (map.get("page") != null && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (map.get("limit") != null && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<DoctorUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_cancel", 0);
        if (map.get("name") != null && !"".equals(map.get("name"))) {
            queryWrapper.like("name", map.get("name").toString());
        }
        if (map.get("mobile") != null && !"".equals(map.get("mobile"))) {
            queryWrapper.like("mobile", map.get("mobile").toString());
        }
        queryWrapper.orderByDesc("create_time");

        IPage<DoctorUser> iPage = baseMapper.selectPage(new Page<>(page, limit), queryWrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }
}
