package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.mapper.LoginRecordMapper;
import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Map;

@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements LoginRecordService {
    @Resource
    private LoginRecordMapper loginRecordMapper;

    @Override
    public PageResult<LoginRecord> getList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<LoginRecord> wrapper = new QueryWrapper<LoginRecord>();
        if (null != map.get("startDate") && !"".equals(map.get("startDate"))&&null != map.get("endDate") && !"".equals(map.get("endDate"))) {
            wrapper.between("created_date",map.get("startDate").toString(),map.get("endDate").toString());
        }
        wrapper.orderByDesc("created_date");
        IPage<LoginRecord> iPage = this.loginRecordMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }
}