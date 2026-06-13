package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.DicMedicines;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 药品目录字典表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
public interface DicMedicinesService extends IService<DicMedicines> {

    PageResult<DicMedicines> page(Map<String,Object> map);

    void dicMedicinesOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}
