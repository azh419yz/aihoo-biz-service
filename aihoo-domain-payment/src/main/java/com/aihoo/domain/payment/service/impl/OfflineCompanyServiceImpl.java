package com.aihoo.domain.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.OfflineCompany;
import com.aihoo.domain.payment.model.mapper.OfflineCompanyMapper;
import com.aihoo.domain.payment.model.vo.ExcelOfflineCompany;
import com.aihoo.domain.payment.service.OfflineCompanyService;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公司管理
 * @author lenovo
 */
@Service
public class OfflineCompanyServiceImpl extends ServiceImpl<OfflineCompanyMapper, OfflineCompany> implements OfflineCompanyService {
    @Resource
    private OfflineCompanyMapper offlineCompanyMapper;

    @Override
    public PageResult<OfflineCompany> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        LambdaQueryWrapper<OfflineCompany> wrapper = new QueryWrapper<OfflineCompany>().lambda();
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like(OfflineCompany::getName, String.valueOf(map.get("name")));
        }
        wrapper.eq(OfflineCompany::getIsDelete, "0");
        IPage<OfflineCompany> iPage = this.page(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public OfflineCompany details(Map<String, Object> map) {
        return this.getById(String.valueOf(map.get("id")));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Map<String, Object> map) {
        LambdaQueryWrapper<OfflineCompany> lambda = new QueryWrapper<OfflineCompany>().lambda();
        lambda.eq(OfflineCompany::getId, String.valueOf(map.get("id")));
        lambda.eq(OfflineCompany::getIsDelete, "0");
        OfflineCompany offlineCompany = new OfflineCompany();
        offlineCompany.setIsDelete("1");
        return this.update(offlineCompany, lambda);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Map<String, Object> map) {
        LambdaQueryWrapper<OfflineCompany> lambda = new QueryWrapper<OfflineCompany>().lambda();
        lambda.eq(OfflineCompany::getId, String.valueOf(map.get("id")));
        lambda.eq(OfflineCompany::getIsDelete, "0");
        OfflineCompany offlineCompany = new OfflineCompany();
        offlineCompany.setName(map.get("name").toString());
        return this.update(offlineCompany, lambda);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONArray bulkImport(List<ExcelOfflineCompany> offlineCompanies, HttpServletRequest request) {
        String startLine = "2";
        JSONArray res = new JSONArray();
        offlineCompanies.stream().forEach(offlineCompany -> {
            if (StrUtil.isBlank(offlineCompany.getName())) {
                res.add("第" + startLine + "行：就诊人姓名不能为空！");
            }
        });
        if (res.size() > 0) {
            return res;
        }
        List<OfflineCompany> addBatch = new ArrayList<>();
        offlineCompanies.stream().forEach(OfflineBlacklist -> {
            OfflineCompany company = new OfflineCompany();
            BeanUtil.copyProperties(OfflineBlacklist, company);
            addBatch.add(company);
        });
        this.saveBatch(addBatch);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Map<String, Object> map) {
        OfflineCompany company = new OfflineCompany();
        company.setName(map.get("name").toString());
        return this.save(company);
    }

    @Override
    public OfflineCompany finnName(List<String> names) {
        if (!CollectionUtils.isEmpty(names)) {
            for (String name : names) {
                QueryWrapper<OfflineCompany> wrapper = new QueryWrapper<>();
                wrapper.eq("name", name);
                wrapper.eq("is_delete", 0);
                List<OfflineCompany> list = this.offlineCompanyMapper.selectList(wrapper);
                if (!CollectionUtils.isEmpty(list)) {
                    for (OfflineCompany company : list) {
                        return  company;
                    }
                }
                return null;
            }
        }
        return null;
    }
}