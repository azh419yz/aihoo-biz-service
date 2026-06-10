package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.mapper.DicHospitalMapper;
import com.aihoo.domain.sys.model.entity.DicHospital;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DicHospitalServiceImpl extends ServiceImpl<DicHospitalMapper, DicHospital> implements DicHospitalService {

    @Autowired
    private DicHospitalMapper dicHospitalMapper;

    @Override
    public PageResult<DicHospital> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<DicHospital> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("create_time");
        IPage<DicHospital> iPage = dicHospitalMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }
}