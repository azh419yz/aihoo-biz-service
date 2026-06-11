package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.hospital.model.excel.DrugEntity;
import com.aihoo.domain.hospital.model.request.SaveUpdateDrugRequest;
import com.aihoo.domain.hospital.model.request.SearchDrugRequest;
import com.aihoo.domain.hospital.model.vo.DrugVo;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 药品信息表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-19
 */
public interface DrugService extends IService<Drug> {

    PageResult<Drug> list(Map<String, Object> map);

    Boolean updateStatus(Map<String, Object> req, HttpServletRequest request);

    JSONObject updateDurg(Map<String, Object> req, HttpServletRequest request);

    void drugBulkExport(String name, String manufacturers, HttpServletRequest request, HttpServletResponse response);

    JSONArray drugBulkImport(List<DrugEntity> drugEntities, HttpServletRequest request);

    JSONObject insert(Map<String, Object> map, HttpServletRequest request);

    // 新规范方法
    PageResult<DrugVo> getPage(PageParam<Drug> pageParam, SearchDrugRequest request);

    boolean create(SaveUpdateDrugRequest request);

    boolean update(SaveUpdateDrugRequest request);

    boolean delete(String id);

    boolean updateStatus(String id, String status);
}
