package com.aihoo.api.doctor.app.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.request.SearchDrugRequest;
import com.aihoo.api.doctor.app.controller.vo.DrugVo;
import com.aihoo.api.doctor.app.model.Drug;

/**
 * <p>
 * 药品信息表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-19
 */
public interface DrugService extends IService<Drug> {
    PageResult<DrugVo> getPage(PageParam<Drug> pageParam, SearchDrugRequest request);
}
