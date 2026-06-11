package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.DrugCount;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 药品对账表 服务类
 * </p>
 *
 * @author lx
 * @since 2020-11-04
 */
public interface DrugCountService extends IService<DrugCount> {

    PageResult<DrugCount> page(Map<String,Object> map) throws ParseException;

    void drugCountOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);
}
