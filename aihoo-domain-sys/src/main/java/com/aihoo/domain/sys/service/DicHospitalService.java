package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.DicHospital;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DicHospitalService extends IService<DicHospital> {

    PageResult<DicHospital> page(Map<String,Object> map);
}