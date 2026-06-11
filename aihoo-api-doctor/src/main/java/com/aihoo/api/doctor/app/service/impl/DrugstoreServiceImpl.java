package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.mapper.DrugstoreMapper;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.model.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.hospital.model.entity.DrugstoreProvinceRel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.request.SearchDrugstoreRequest;
import com.aihoo.api.doctor.app.controller.vo.DrugstoreVo;
import com.aihoo.api.doctor.app.service.DrugstoreMedicineStatusRelService;
import com.aihoo.api.doctor.app.service.DrugstoreProvinceRelService;
import com.aihoo.api.doctor.app.service.DrugstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrugstoreServiceImpl extends ServiceImpl<DrugstoreMapper, Drugstore> implements DrugstoreService {
    private final DrugstoreProvinceRelService provinceRelService;
    private final DrugstoreMedicineStatusRelService medicineStatusRelService;


    @Override
    public PageResult<DrugstoreVo> getPage(PageParam<Drugstore> pageParam, SearchDrugstoreRequest request) {
        List<String> idListByProvince = null;
        if (StringUtil.isNotBlank(request.getProvincesCode())) {
            idListByProvince = provinceRelService.list(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                            .eq(DrugstoreProvinceRel::getProvinceCode, request.getProvincesCode()))
                    .stream()
                    .map(DrugstoreProvinceRel::getDrugstoreId)
                    .toList();
            if (CollectionUtils.isEmpty(idListByProvince)) {
                return new PageResult<>();
            }
        }

        List<String> idListByMedicineStatus = null;
        if (!CollectionUtils.isEmpty(request.getMedicineStatusList())) {
            idListByMedicineStatus = medicineStatusRelService.listObjs(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                    .select(DrugstoreMedicineStatusRel::getDrugstoreId)
                    .in(DrugstoreMedicineStatusRel::getMedicineStatusCode, request.getMedicineStatusList())
                    .groupBy(DrugstoreMedicineStatusRel::getDrugstoreId)
                    .having("COUNT(DISTINCT medicine_status_code) = {0}", request.getMedicineStatusList().size()))
                    .stream()
                    .map(String::valueOf)
                    .toList();
            if (CollectionUtils.isEmpty(idListByMedicineStatus)) {
                return new PageResult<>();
            }
        }

        List<String> finalIdList = null;
        if (!CollectionUtils.isEmpty(idListByProvince) && !CollectionUtils.isEmpty(idListByMedicineStatus)) {
            // 计算交集
            finalIdList = idListByProvince.stream()
                    .filter(idListByMedicineStatus::contains)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(finalIdList)) {
                return new PageResult<>();
            }
        } else if (!CollectionUtils.isEmpty(idListByProvince)) {
            finalIdList = idListByProvince;
        } else if (!CollectionUtils.isEmpty(idListByMedicineStatus)) {
            finalIdList = idListByMedicineStatus;
        }

        LambdaQueryWrapper<Drugstore> queryWrapper = new LambdaQueryWrapper<Drugstore>()
                .like(StringUtil.isNotBlank(request.getName()), Drugstore::getName, request.getName())
                .in(!CollectionUtils.isEmpty(finalIdList), Drugstore::getId, finalIdList)
                .orderByDesc(Drugstore::getCreateTime);

        // 执行分页查询
        Page<Drugstore> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }

        List<String> drugstoreIdList = page.getRecords().stream().map(Drugstore::getId).toList();
        Map<String, List<Integer>> medicineStatusCodeMap = medicineStatusRelService.list(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                        .in(DrugstoreMedicineStatusRel::getDrugstoreId, drugstoreIdList))
                .stream()
                .collect(Collectors.groupingBy(DrugstoreMedicineStatusRel::getDrugstoreId, Collectors.mapping(DrugstoreMedicineStatusRel::getMedicineStatusCode, Collectors.toList())));

        Map<String, List<String>> provinceCodeMap = provinceRelService.list(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                        .in(DrugstoreProvinceRel::getDrugstoreId, drugstoreIdList))
                .stream()
                .collect(Collectors.groupingBy(DrugstoreProvinceRel::getDrugstoreId, Collectors.mapping(DrugstoreProvinceRel::getProvinceCode, Collectors.toList())));

        List<DrugstoreVo> voList = new ArrayList<>();
        for (Drugstore drugstore : page.getRecords()) {
            DrugstoreVo vo = new DrugstoreVo();
            BeanUtils.copyProperties(drugstore, vo);
            vo.setProvinceList(provinceCodeMap.get(drugstore.getId()));
            vo.setMedicineStatusList(medicineStatusCodeMap.get(drugstore.getId()));
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }
}
