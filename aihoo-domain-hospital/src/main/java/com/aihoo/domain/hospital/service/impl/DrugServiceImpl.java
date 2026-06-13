package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.dto.DrugVo;
import com.aihoo.domain.hospital.dto.SearchDrugRequest;
import com.aihoo.domain.hospital.model.mapper.DrugMapper;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.hospital.service.DrugService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service("drugServiceImpl")
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {

    @Override
    public PageResult<Object> getPage(PageParam<Drug> pageParam, SearchDrugRequest request) {

        LambdaQueryWrapper<Drug> queryWrapper = new LambdaQueryWrapper<Drug>()
                .eq(Drug::getDrugstoreId, request.getDrugstoreId())
                .eq(Drug::getStatus, "1")
                .like(StringUtil.isNotBlank(request.getName()), Drug::getName, request.getName())
                .likeRight(StringUtil.isNotBlank(request.getInitial()), Drug::getPinyinInitial, request.getInitial().toUpperCase())
                .orderByAsc(Drug::getPrice);

        Page<Drug> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<Object>();
        }
        List<DrugVo> voList = new ArrayList<>();
        for (Drug drug : page.getRecords()) {
            DrugVo vo = new DrugVo();
            BeanUtils.copyProperties(drug, vo);
            voList.add(vo);
        }
        return (PageResult<Object>) (Object) new PageResult<>(voList, page.getTotal());
    }
}
