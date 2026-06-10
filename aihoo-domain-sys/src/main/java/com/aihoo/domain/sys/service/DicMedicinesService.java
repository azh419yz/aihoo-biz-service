package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.DicMedicines;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DicMedicinesService extends IService<DicMedicines> {

    PageResult<DicMedicines> page(Map<String,Object> map);
}