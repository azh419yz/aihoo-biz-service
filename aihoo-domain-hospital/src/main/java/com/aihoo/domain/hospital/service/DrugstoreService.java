package com.aihoo.domain.hospital.service;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.DrugstoreVo;
import com.aihoo.domain.hospital.dto.SearchDrugstoreRequest;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DrugstoreService extends IService<Drugstore> {
    PageResult<Object> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequest request);
}
