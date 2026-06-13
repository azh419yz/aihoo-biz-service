package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.DrugVo;
import com.aihoo.domain.hospital.dto.SearchDrugRequest;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 药品信息表 服务类
 */
public interface DrugService extends IService<Drug> {
    PageResult<Object> getPage(PageParam<Drug> pageParam, SearchDrugRequest request);
}
