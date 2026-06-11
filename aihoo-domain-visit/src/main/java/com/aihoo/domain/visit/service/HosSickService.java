package com.aihoo.domain.visit.service;


import com.aihoo.common.PageResult;
import com.aihoo.domain.visit.model.entity.HosSick;
import com.aihoo.domain.visit.model.vo.HosSickVo;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 就诊人信息表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
public interface HosSickService extends IService<HosSick> {

    int getCount(Map<String, Object> map);

    JSONArray hosSickList(Map<String, Object> map);

    JSONObject hosSickDetails(String id);

    void hosSickBulkExport(String name, String mobile, HttpServletRequest request, HttpServletResponse response);

     PageResult<HosSickVo> page(Map<String, Object> map);
}
