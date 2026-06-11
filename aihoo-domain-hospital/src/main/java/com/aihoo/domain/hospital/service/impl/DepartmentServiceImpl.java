package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.constant.ReportCodingEnum;
import com.aihoo.domain.hospital.model.entity.Department;
import com.aihoo.domain.hospital.model.mapper.DepartmentMapper;
import com.aihoo.domain.hospital.service.DepartmentService;
import com.aihoo.domain.sys.model.entity.DicDepartment;
import com.aihoo.domain.sys.model.mapper.DicDepartmentMapper;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Classname DepartmentServiceImpl
 * @Description hf
 * @Date 2020/9/18 17:15
 * @Created by ad
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private DicDepartmentMapper dicDepartmentMapper;


    @Override
    public String findDepartmentNameByCode(String code) {
        List<Department> list = departmentMapper.selectList(new QueryWrapper<Department>().eq("code", code));
        return list.get(0).getName();
    }

    @Override
    public PageResult<Department> departmentsList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        if (null != map.get("code") && !"".equals(map.get("code"))) {
            wrapper.eq("code", map.get("code"));
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            wrapper.like("name", map.get("name"));
        }
        IPage<Department> iPage = this.departmentMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(Map<String, Object> map) {
        JSONObject res = new JSONObject();
        Department department = new Department();
        String level;
        if (null != map.get("iconImg") && !"".equals(map.get("iconImg"))) {
            department.setIconImg(map.get("iconImg").toString().trim());
        }
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            department.setIndex(map.get("index").toString().trim());
        }
        if (null != map.get("parentCode") && !"".equals(map.get("parentCode"))) {
            department.setParentCode(map.get("parentCode").toString());
            level = "2";
            department.setLevel(level);
        } else {
            level = "1";
            department.setLevel(level);
        }
        List<Department> list = this.departmentMapper.selectList(new QueryWrapper<Department>().eq("code", map.get("code")));
        if (!CollectionUtils.isEmpty(list)) {
            res.put("msg", "该科室的code已经存在");
            return res;
        }
        // 一级科室code为空
        department.setName(map.get("name").toString().trim());
        department.setCode(map.get("code").toString().trim());
        this.departmentMapper.insert(department);
        // 互联网上报数据写入
        DicDepartment dicDepartment = new DicDepartment();
        dicDepartment.setYljgdm(ReportCodingEnum.YLJGDM);
        dicDepartment.setWsjdm(map.get("code").toString());
        dicDepartment.setWsjgdm(ReportCodingEnum.WSJGDM);
        dicDepartment.setKstybz("1");
        dicDepartment.setKsjb(level);
        dicDepartment.setSfkzyy("1");
        dicDepartment.setXgbz(ReportCodingEnum.XGBZ);
        dicDepartment.setDepartmentId(department.getId());
        this.dicDepartmentMapper.insert(dicDepartment);
        return res;
    }

    @Override
    public JSONObject departmentUpdate(Map<String, Object> map) {
        JSONObject res = new JSONObject();
        Department department = new Department();
        if (null == map.get("id") || "".equals(map.get("id"))) {
            res.put("msg", "科室id必填");
            return res;
        }
        if (null != map.get("name") && !"".equals(map.get("name"))) {
            department.setName(map.get("name").toString().trim());
        }
        if (null != map.get("iconImg") && !"".equals(map.get("iconImg"))) {
            department.setIconImg(map.get("iconImg").toString());
        }
        if (null != map.get("index") && !"".equals(map.get("index"))) {
            department.setIndex(map.get("index").toString().trim());
        }
        if (null != map.get("parentCode") && !"".equals(map.get("parentCode"))) {
            department.setParentCode(map.get("parentCode").toString().trim());
        }
        department.setId(map.get("id").toString());
        this.departmentMapper.updateById(department);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void departmentEnableDisable(Map<String, Object> map) {
        Department department = new Department();
        department.setId(map.get("id").toString());
        department.setStatus(map.get("status").toString());
        this.departmentMapper.updateById(department);
        DicDepartment dicDepartment = new DicDepartment();
        dicDepartment.setKstybz(map.get("status").toString());
        QueryWrapper<DicDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", map.get("id").toString());
        this.dicDepartmentMapper.update(dicDepartment, wrapper);
    }
}
