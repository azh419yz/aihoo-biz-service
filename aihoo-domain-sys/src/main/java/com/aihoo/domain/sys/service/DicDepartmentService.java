package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.DicDepartment;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DicDepartmentService extends IService<DicDepartment> {

    PageResult<DicDepartment> page(Map<String,Object> map);
}