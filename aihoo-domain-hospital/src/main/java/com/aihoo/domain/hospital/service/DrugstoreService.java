package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.model.request.SaveUpdateDrugstoreRequest;
import com.aihoo.domain.hospital.model.request.SearchDrugstoreRequest;
import com.aihoo.domain.hospital.model.vo.DrugstoreVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface DrugstoreService extends IService<Drugstore> {

    /**
     * 带条件分页查询药房列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @param params   查询参数
     * @return 分页结果
     */
    IPage<Drugstore> getPage(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 带条件分页查询药房列表
     *
     * @param pageParam 分页参数
     * @return 分页结果
     */
    PageResult<DrugstoreVo> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequest request);

    /**
     * 创建药房信息
     *
     * @param request 药房信息
     * @return 是否创建成功
     */
    boolean create(SaveUpdateDrugstoreRequest request);

    /**
     * 修改药房信息
     *
     * @param request 药房信息
     * @return 是否修改成功
     */
    boolean update(SaveUpdateDrugstoreRequest request);

    /**
     * 删除药房信息
     *
     * @param id 药房ID
     * @return 是否删除成功
     */
    boolean delete(String id);

    /**
     * 启用禁用药房
     *
     * @param id     药房ID
     * @param status 状态（1:启用 0:停用）
     * @return 是否操作成功
     */
    boolean updateStatus(String id, String status);
}
