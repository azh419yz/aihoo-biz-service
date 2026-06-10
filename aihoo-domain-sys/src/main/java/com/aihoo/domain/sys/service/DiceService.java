package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.common.JsonResult;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Classname DiceService
 * @Description hf
 * @Date 2020/9/15 20:59
 * @Created by ad
 */
public interface DiceService extends IService<Dict> {

    JSONArray getDoctorType(String type);

    Dict getDoctorNameByCodeAndType(String type);


    String getDoctorNameByTypeAndCode(String type, String code);

    String getDoctorNameByTypeAndName(String type, String name);

    List<Map<String, String>> getMdtTypeList();

    JsonResult listByType(String type);
}
