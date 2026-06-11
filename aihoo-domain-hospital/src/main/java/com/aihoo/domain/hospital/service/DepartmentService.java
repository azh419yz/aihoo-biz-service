package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Department;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Classname DepartmentService
 * @Description hf
 * @Date 2020/9/18 17:14
 * @Created by ad
 */

public interface DepartmentService extends IService<Department> {
    String findDepartmentNameByCode(String code);

    PageResult<Department> departmentsList(Map<String, Object> map);

    JSONObject add(Map<String, Object> map);

    JSONObject departmentUpdate(Map<String, Object> map);

    void departmentEnableDisable(Map<String, Object> map);
}
