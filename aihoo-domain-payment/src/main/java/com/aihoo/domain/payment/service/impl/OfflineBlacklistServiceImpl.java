package com.aihoo.domain.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.OfflineBlacklist;
import com.aihoo.domain.payment.model.entity.OfflineHospital;
import com.aihoo.domain.payment.model.mapper.OfflineBlacklistMapper;
import com.aihoo.domain.payment.model.mapper.OfflineOderMapper;
import com.aihoo.domain.payment.service.OfflineBlacklistService;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OfflineBlacklistServiceImpl extends ServiceImpl<OfflineBlacklistMapper, OfflineBlacklist> implements OfflineBlacklistService {

    @Resource
    private OfflineOderMapper oderMapper;

    @Resource
    private OfflineBlacklistMapper mapper;

    /**
     * 列表
     */
    @Override
    public PageResult<OfflineBlacklist> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        LambdaQueryWrapper<OfflineBlacklist> wrapper = new QueryWrapper<OfflineBlacklist>().lambda();
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like(OfflineBlacklist::getName, String.valueOf(map.get("name")));
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            wrapper.like(OfflineBlacklist::getPhone, String.valueOf(map.get("phone")));
        }
        if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
            wrapper.like(OfflineBlacklist::getHospitalName, String.valueOf(map.get("hospitalName")));
        }
        wrapper.eq(OfflineBlacklist::getIsDelete, "0");
        IPage<OfflineBlacklist> iPage = this.page(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<String> ids) {
        List<OfflineBlacklist> offlineBlacklists = Lists.newArrayList();
        ids.forEach(id -> {
            OfflineBlacklist offlineBlacklist = new OfflineBlacklist();
            offlineBlacklist.setId(id);
            offlineBlacklist.setIsDelete("1");
            offlineBlacklists.add(offlineBlacklist);
        });
        return this.updateBatchById(offlineBlacklists);
    }

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONArray insert(Map<String, Object> map) {
        JSONArray res = new JSONArray();
        QueryWrapper<OfflineBlacklist> wrapper = new QueryWrapper<>();
        wrapper.eq("certificates", map.get("certificates"));
        wrapper.eq("hospital_name",map.get("hospitalName"));
        wrapper.eq("name",map.get("name"));
        OfflineBlacklist one = this.mapper.selectOne(wrapper);
        if (one == null) {
            OfflineBlacklist blacklist = new OfflineBlacklist();
            blacklist.setName(map.get("name").toString());
            blacklist.setPhone(map.get("phone").toString());
            blacklist.setCertificates(map.get("certificates").toString());
            blacklist.setHospitalId(map.get("hospitalId").toString());
            blacklist.setHospitalName(map.get("hospitalName").toString());
            blacklist.setRealDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            this.save(blacklist);
        }
        res.add("该就诊人已存在黑名单了");
        return res;
    }

    /**
     * 查询医院的id和名称
     */
    @Override
    public List<OfflineHospital> selectHospital() {
        return oderMapper.selectHospitalCard();
    }

    /**
     * 导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONArray bulkImport(List<OfflineBlacklist> offlineCompanies, HttpServletRequest request) {
        String startLine = "2";
        JSONArray res = new JSONArray();
        offlineCompanies.stream().forEach(offlineCompany -> {
            if (StrUtil.isBlank(offlineCompany.getName())) {
                res.add("第" + startLine + "行：就诊人姓名不能为空！");
            }
            if (StrUtil.isBlank(offlineCompany.getPhone())) {
                res.add("第" + startLine + "行：手机号不能为空！");
            }
            if (StrUtil.isBlank(offlineCompany.getCertificates())) {
                res.add("第" + startLine + "行：证件号码不能为空！");
            }
            if (StrUtil.isBlank(offlineCompany.getHospitalName())) {
                res.add("第" + startLine + "行：医院名称不能为空！");
            }
        });
        if (res.size() > 0) {
            return res;
        }
        List<OfflineBlacklist> addBatch = new ArrayList<>();
        for (OfflineBlacklist offlineBlacklist : offlineCompanies) {
            OfflineBlacklist company = new OfflineBlacklist();
            BeanUtil.copyProperties(offlineBlacklist, company);
            if (null == offlineBlacklist.getHospitalName() && "".equals(offlineBlacklist.getHospitalName())) {
                throw new NullPointerException();
            }
            OfflineHospital hospital = oderMapper.findOneHospital(offlineBlacklist.getHospitalName());
            if (null == hospital) {
                res.add("该医院不存在");
                return res;
            }
            QueryWrapper<OfflineBlacklist> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("name", offlineBlacklist.getName());
            wrapper1.eq("certificates", offlineBlacklist.getCertificates());
            wrapper1.eq("hospital_name", offlineBlacklist.getHospitalName());
            OfflineBlacklist selectOne = mapper.selectOne(wrapper1);
            if (null != selectOne) {
                res.add("已经存在重复的数据了");
                return res;
            }
            company.setHospitalId(hospital.getHospitalId());
            company.setRealDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            addBatch.add(company);
            this.saveBatch(addBatch);
        }
        return res;
    }

    /**
     * 根据就诊人身份证拆线呢是否在黑名单李
     */
    @Override
    public boolean findBlackName(Map<String, Object> map) {
        transformationMap(map);
        QueryWrapper<OfflineBlacklist> wrapper = new QueryWrapper<>();
        wrapper.eq("certificates", map.get("certificates"));
        wrapper.eq("hospital_name",map.get("hospitalName"));
        wrapper.eq("is_delete", "0");
        OfflineBlacklist black = this.mapper.selectOne(wrapper);
        return black == null;
    }

    private static void transformationMap(Map<String, Object> map) {
        if ("复旦大学附属华山医院(总院)".equals(map.get("hospitalName")) || "复旦大学附属华山医院(江苏路分部)".equals(map.get("hospitalName")) ||
                "复旦大学附属华山医院(传染科大楼)".equals(map.get("hospitalName")) || "复旦大学附属华山医院(西院)".equals(map.get("hospitalName"))) {
            map.put("hospitalName", "复旦大学附属华山医院");
        }
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateBlack(Map<String, Object> map) {
        OfflineBlacklist blacklist = new OfflineBlacklist();
        QueryWrapper<OfflineBlacklist> wrapper = new QueryWrapper<>();
        wrapper.eq("id", map.get("id"));
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            blacklist.setName(map.get("name").toString());
        }
        if (null != map.get("phone") && !"".equals(map.get("phone"))) {
            blacklist.setPhone(map.get("phone").toString());
        }
        if (null != map.get("certificates") && !"".equals(map.get("certificates"))) {
            blacklist.setCertificates(map.get("certificates").toString());
        }
        if (null != map.get("hospitalName") && !"".equals(map.get("hospitalName"))) {
            blacklist.setHospitalName(map.get("hospitalName").toString());
        }
        return this.mapper.update(blacklist,wrapper) == 1;
    }

}