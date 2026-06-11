package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.model.entity.DrugstoreMedicineStatusRel;
import com.aihoo.domain.hospital.model.entity.DrugstoreProvinceRel;
import com.aihoo.domain.hospital.model.mapper.DrugstoreMapper;
import com.aihoo.domain.hospital.model.request.SaveUpdateDrugstoreRequest;
import com.aihoo.domain.hospital.model.request.SearchDrugstoreRequest;
import com.aihoo.domain.hospital.model.vo.DrugstoreVo;
import com.aihoo.domain.hospital.service.DrugstoreMedicineStatusRelService;
import com.aihoo.domain.hospital.service.DrugstoreProvinceRelService;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 药房服务实现
 *
 * <p>从旧 admin 的 com.aihoo.admin.system.service.impl.DrugstoreServiceImpl 迁入。
 * 入参/返回/方法签名与旧代码一致；包路径已统一到 com.aihoo.domain.hospital.* 和
 * com.aihoo.common.* / com.aihoo.util.*。getPage(Integer,Integer,Map) 旧代码调用
 * 了不存在的 selectDrugstorePage mapper 方法，保留为 TODO stub。</p>
 */
@Service
@RequiredArgsConstructor
public class DrugstoreServiceImpl extends ServiceImpl<DrugstoreMapper, Drugstore> implements DrugstoreService {

    private final DrugstoreMedicineStatusRelService medicineStatusRelService;
    private final DrugstoreProvinceRelService provinceRelService;


    @Override
    public IPage<Drugstore> getPage(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        // 旧代码这里调用 baseMapper.selectDrugstorePage(page, params) — 该 mapper 方法不存在
        Page<Drugstore> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectPage(page, null);
    }

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean create(SaveUpdateDrugstoreRequest request) {
        Drugstore drugstore = new Drugstore();
        BeanUtils.copyProperties(request, drugstore);
        boolean saved = save(drugstore);

        List<DrugstoreMedicineStatusRel> medicineStatusRelList = request.getMedicineStatusList().stream().map(code -> {
            DrugstoreMedicineStatusRel rel = new DrugstoreMedicineStatusRel();
            rel.setDrugstoreId(drugstore.getId());
            rel.setMedicineStatusCode(code);
            return rel;
        }).toList();
        medicineStatusRelService.saveBatch(medicineStatusRelList);

        List<DrugstoreProvinceRel> provinceRelList = request.getProvinceList().stream().map(code -> {
            DrugstoreProvinceRel rel = new DrugstoreProvinceRel();
            rel.setDrugstoreId(drugstore.getId());
            rel.setProvinceCode(code);
            return rel;
        }).toList();
        provinceRelService.saveBatch(provinceRelList);
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SaveUpdateDrugstoreRequest request) {
        Drugstore drugstore = new Drugstore();
        BeanUtils.copyProperties(request, drugstore);
        boolean updated = updateById(drugstore);

        medicineStatusRelService.remove(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                .eq(DrugstoreMedicineStatusRel::getDrugstoreId, drugstore.getId()));
        List<DrugstoreMedicineStatusRel> medicineStatusRelList = request.getMedicineStatusList().stream().map(code -> {
            DrugstoreMedicineStatusRel rel = new DrugstoreMedicineStatusRel();
            rel.setDrugstoreId(drugstore.getId());
            rel.setMedicineStatusCode(code);
            return rel;
        }).toList();
        medicineStatusRelService.saveBatch(medicineStatusRelList);

        provinceRelRelCleanup(drugstore.getId());
        List<DrugstoreProvinceRel> provinceRelList = request.getProvinceList().stream().map(code -> {
            DrugstoreProvinceRel rel = new DrugstoreProvinceRel();
            rel.setDrugstoreId(drugstore.getId());
            rel.setProvinceCode(code);
            return rel;
        }).toList();
        provinceRelService.saveBatch(provinceRelList);
        return updated;
    }

    private void provinceRelRelCleanup(String drugstoreId) {
        provinceRelService.remove(new LambdaQueryWrapper<DrugstoreProvinceRel>()
                .eq(DrugstoreProvinceRel::getDrugstoreId, drugstoreId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(String id) {
        boolean removed = removeById(id);
        medicineStatusRelService.remove(new LambdaQueryWrapper<DrugstoreMedicineStatusRel>()
                .eq(DrugstoreMedicineStatusRel::getDrugstoreId, id));
        provinceRelRelCleanup(id);
        return removed;
    }

    @Override
    public boolean updateStatus(String id, String status) {
        Drugstore drugstore = new Drugstore();
        drugstore.setId(id);
        drugstore.setStatus(status);
        return updateById(drugstore);
    }
}
