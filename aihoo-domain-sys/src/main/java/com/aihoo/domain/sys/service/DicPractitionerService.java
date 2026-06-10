package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.DicPractitioner;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DicPractitionerService extends IService<DicPractitioner> {

    PageResult<DicPractitioner> page(Map<String,Object> map);
}