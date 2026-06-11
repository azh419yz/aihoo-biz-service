package com.aihoo.api.doctor.app.service;


import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.request.SearchDrugstoreRequest;
import com.aihoo.api.doctor.app.controller.vo.DrugstoreVo;

public interface DrugstoreService extends IService<Drugstore> {
    PageResult<DrugstoreVo> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequest request);

}
