package com.aihoo.domain.payment.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.OfflineBlacklist;
import com.aihoo.domain.payment.model.entity.OfflineHospital;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface OfflineBlacklistService extends IService<OfflineBlacklist> {

    /**
     * 黑名单列表
     */
    PageResult<OfflineBlacklist> list(Map<String, Object> map);

    /**
     * 删除
     */
    boolean deleteBatch(List<String> ids);

    /**
     * 新增
     */
    JSONArray insert(Map<String, Object> map);

    /**
     * 查询医院的名称和id
     */
    List<OfflineHospital> selectHospital();

    /**
     * 导入数据
     */
    JSONArray bulkImport(List<OfflineBlacklist> offlineCompanies, HttpServletRequest request);

    /**
     * 根据就诊人查询用户的名称
     */
    boolean findBlackName(Map<String, Object> map);

    /**
     * 修改黑名单数据
     */
    boolean updateBlack(Map<String, Object> map);
}