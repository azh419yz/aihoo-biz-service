package com.aihoo.api.doctor.app.service.impl;


import com.aihoo.util.StringUtil;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.request.SearchDrugRequest;
import com.aihoo.api.doctor.app.controller.vo.DrugVo;
import com.aihoo.api.doctor.app.mapper.DrugMapper;
import com.aihoo.api.doctor.app.model.Drug;
import com.aihoo.api.doctor.app.service.DrugService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>r
 * 药品信息表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-09-19
 */
@Service
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {


    @Override
    public PageResult<DrugVo> getPage(PageParam<Drug> pageParam, SearchDrugRequest request) {

        LambdaQueryWrapper<Drug> queryWrapper = new LambdaQueryWrapper<Drug>()
                .eq(Drug::getDrugstoreId, request.getDrugstoreId())
                .eq(Drug::getStatus, "1")
                .like(StringUtil.isNotBlank(request.getName()), Drug::getName, request.getName())
                .likeRight(StringUtil.isNotBlank(request.getInitial()), Drug::getPinyinInitial, request.getInitial().toUpperCase())
                .orderByAsc(Drug::getPrice);

        // 执行分页查询
        Page<Drug> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }
        List<DrugVo> voList = new ArrayList<>();
        for (Drug drug : page.getRecords()) {
            DrugVo vo = new DrugVo();
            BeanUtils.copyProperties(drug, vo);
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

}
