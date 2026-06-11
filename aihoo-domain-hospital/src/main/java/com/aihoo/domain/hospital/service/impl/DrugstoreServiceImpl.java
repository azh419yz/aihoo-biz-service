package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.model.mapper.DrugstoreMapper;
import com.aihoo.domain.hospital.model.request.SaveUpdateDrugstoreRequest;
import com.aihoo.domain.hospital.model.request.SearchDrugstoreRequest;
import com.aihoo.domain.hospital.model.vo.DrugstoreVo;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 药房服务实现
 *
 * <p>当前状态：占位实现，方法签名已与旧代码对齐；具体业务逻辑待完整迁移。</p>
 */
@Service
public class DrugstoreServiceImpl extends ServiceImpl<DrugstoreMapper, Drugstore> implements DrugstoreService {

    @Override
    public IPage<Drugstore> getPage(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugstoreServiceImpl.getPage 完整实现");
    }

    @Override
    public PageResult<DrugstoreVo> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugstoreServiceImpl.getPage(PageParam,SearchDrugstoreRequest) 完整实现");
    }

    @Override
    public boolean create(SaveUpdateDrugstoreRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugstoreServiceImpl.create 完整实现");
    }

    @Override
    public boolean update(SaveUpdateDrugstoreRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugstoreServiceImpl.update 完整实现");
    }

    @Override
    public boolean delete(String id) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugstoreServiceImpl.delete 完整实现");
    }

    @Override
    public boolean updateStatus(String id, String status) {
        throw new UnsupportedOperationException("TODO: 迁移 DrugstoreServiceImpl.updateStatus 完整实现");
    }
}
