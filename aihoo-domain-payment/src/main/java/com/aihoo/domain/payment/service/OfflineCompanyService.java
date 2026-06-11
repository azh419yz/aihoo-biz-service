package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.OfflineCompany;
import com.aihoo.domain.payment.model.vo.ExcelOfflineCompany;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 公司管理
 *
 * @author lenovo
 */
public interface OfflineCompanyService extends IService<OfflineCompany> {
    PageResult<OfflineCompany> list(Map<String, Object> map);

    OfflineCompany details(Map<String, Object> map);

    boolean delete(Map<String, Object> map);

    boolean update(Map<String, Object> map);

    JSONArray bulkImport(List<ExcelOfflineCompany> offlineCompanies, HttpServletRequest request);

    boolean insert(Map<String, Object> map);

    OfflineCompany finnName(List<String> names);
}