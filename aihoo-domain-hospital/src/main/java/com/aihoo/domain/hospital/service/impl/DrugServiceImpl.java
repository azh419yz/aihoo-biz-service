package com.aihoo.domain.hospital.service.impl;


import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.constant.DictTypeEnum;
import com.aihoo.domain.hospital.constant.ReportCodingEnum;
import com.aihoo.domain.hospital.model.entity.Drug;
import com.aihoo.domain.hospital.model.entity.Drugstore;
import com.aihoo.domain.hospital.model.excel.DrugEntity;
import com.aihoo.domain.hospital.model.mapper.DrugMapper;
import com.aihoo.domain.hospital.model.request.SaveUpdateDrugRequest;
import com.aihoo.domain.hospital.model.request.SearchDrugRequest;
import com.aihoo.domain.hospital.model.vo.DrugVo;
import com.aihoo.domain.hospital.service.DrugService;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.domain.sys.model.entity.DicMedicines;
import com.aihoo.domain.sys.model.entity.Dict;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.mapper.DicMedicinesMapper;
import com.aihoo.domain.sys.model.mapper.DiceMapper;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.domain.sys.service.DicMedicinesService;
import com.aihoo.domain.sys.service.DiceService;
import com.aihoo.domain.sys.util.LoginRecordUtil;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.IdUtils;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.StringUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Resource
    private DrugMapper drugMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private DiceMapper diceMapper;

    @Resource
    @Lazy
    private DrugService drugService;

    @Resource
    private DiceService diceService;

    @Resource
    private LoginRecordUtil loginRecordUtil;

    @Resource
    private DicMedicinesService dicMedicinesService;

    @Resource
    private DicMedicinesMapper dicMedicinesMapper;

    @Resource
    private DrugstoreService drugstoreService;

    @Override
    public PageResult<Drug> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }

        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        if (null != map.get("name") && !"".equals(map.get("name").toString())) {
            wrapper.like("name", map.get("name"));
        }
        if (null != map.get("manufacturers") && !"".equals(map.get("manufacturers").toString())) {
            wrapper.like("manufacturers", map.get("manufacturers"));
        }
        wrapper.orderByDesc("id");
        IPage<Drug> iPage = drugMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateStatus(Map<String, Object> req, HttpServletRequest request) {
        Drug drug = new Drug();
        drug.setId(req.get("id").toString().trim());
        drug.setStatus(req.get("status").toString().trim());
        int i = this.drugMapper.updateById(drug);
        // 更新对应上报数据的使用标志
        DicMedicines dicMedicines = new DicMedicines();
        dicMedicines.setSybz(drug.getStatus());
        this.dicMedicinesMapper.update(dicMedicines, new QueryWrapper<DicMedicines>().eq("drug_id", drug.getId()));
        loginRecordUtil.saveLoginRecord(request, "药品的启用与禁用");
        return i > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject updateDurg(Map<String, Object> map, HttpServletRequest request) {
        JSONObject res = new JSONObject();
        Drug drug = new Drug();
        if (StringUtils.isEmpty(map.get("id"))) {
            res.put("msg", "更新药品信息，必须携带id");
            return res;
        } else {
            drug.setId(map.get("id").toString());
        }
        /*if (!StringUtils.isEmpty(map.get("skuCode"))) {
            Drug selectById = this.drugMapper.selectById(map.get("id").toString());
            if (!selectById.getSkuCode().equals(map.get("skuCode").toString())) {
                Drug existDrug = this.drugService.getOne(new QueryWrapper<Drug>().eq("sku_code", map.get("skuCode")));
                if (!StringUtils.isEmpty(existDrug)) {
                    res.put("msg", "已存在该药物请勿重复添加 重复药物代码：" + map.get("skuCode"));
                    return res;
                }
                drug.setSkuCode(map.get("skuCode").toString());
            }
        }*/
        /*if (!StringUtils.isEmpty(map.get("mshId"))) {
            drug.setMshId(map.get("mshId").toString());
        }*/
        if (!StringUtils.isEmpty(map.get("healthCode"))) {
            Drug selectById = this.drugMapper.selectById(map.get("id").toString());
            if (!selectById.getHealthCode().equals(map.get("healthCode").toString())) {
                Drug drugOne = this.drugService.getOne(new QueryWrapper<Drug>().eq("health_code", map.get("healthCode")));
                if (!StringUtils.isEmpty(drugOne)) {
                    res.put("msg", "已存在该药物请勿重复添加 重复药物代码：" + map.get("healthCode"));
                    return res;
                }
            }
            drug.setHealthCode(map.get("healthCode").toString());
        }
        if (!StringUtils.isEmpty(map.get("name"))) {
            drug.setName(map.get("name").toString());
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            drug.setSize(map.get("size").toString());
        }
        if (!StringUtils.isEmpty(map.get("drugDosCode"))) {
            String drugDosName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.DRUG_DOS.getType(), map.get("drugDosCode").toString());
            if (StringUtils.isEmpty(drugDosName)) {
                res.put("msg", "药品剂型编码错误");
                return res;
            }
            drug.setDrugDosCode(map.get("drugDosCode").toString());
            drug.setDrugDosName(drugDosName);
        }
        if (!StringUtils.isEmpty(map.get("unitMeasure"))) {
            drug.setUnitMeasure(map.get("unitMeasure").toString());
        }

        if (!StringUtils.isEmpty(map.get("doseUnitCode"))) {
            String doseUnitName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.MED_UNIT.getType(), map.get("doseUnitCode").toString());
            if (StringUtils.isEmpty(doseUnitName)) {
                res.put("msg", "剂量单位编码错误");
                return res;
            }
            drug.setDoseUnitCode(map.get("doseUnitCode").toString());
            drug.setDoseUnit(doseUnitName);
        }

        if (!StringUtils.isEmpty(map.get("manufacturers"))) {
            drug.setManufacturers(map.get("manufacturers").toString());
        }
        if (!StringUtils.isEmpty(map.get("approvalNumber"))) {
            drug.setApprovalNumber(map.get("approvalNumber").toString());
        }
        if (!StringUtils.isEmpty(map.get("packUnitCode"))) {
            String packUnitName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.PACK_UNIT.getType(), map.get("packUnitCode").toString());
            if (StringUtils.isEmpty(packUnitName)) {
                res.put("msg", "包装单位编码错误");
                return res;
            }
            drug.setPackUnitCode(map.get("packUnitCode").toString());
            drug.setPackUnitName(packUnitName);
        }
        if (!StringUtils.isEmpty(map.get("price"))) {
            drug.setPrice(map.get("price").toString());
        }
        if (!StringUtils.isEmpty(map.get("freqMedCode"))) {
            String freqMedName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.FREQ_MED.getType(), map.get("freqMedCode").toString());
            if (StringUtils.isEmpty(freqMedName)) {
                res.put("msg", "默认用药频次编码错误");
                return res;
            }
            drug.setFreqMedCode(map.get("freqMedCode").toString());
            drug.setFreqMedName(freqMedName);
        }
        if (!StringUtils.isEmpty(map.get("routeAdmiCode"))) {
            String routeAdmiName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.ROUTE_ADMI.getType(), map.get("routeAdmiCode").toString());
            if (StringUtils.isEmpty(routeAdmiName)) {
                res.put("msg", "默认用药频次编码错误");
                return res;
            }
            drug.setRouteAdmiCode(map.get("routeAdmiCode").toString());
            drug.setRouteAdmiName(routeAdmiName);
        }
        if (!StringUtils.isEmpty(map.get("isBasicMedicine"))) {
            drug.setIsBasicMedicine(map.get("isBasicMedicine").toString());
        }
        if (!StringUtils.isEmpty(map.get("basicMedicineCode"))) {
            String basicMedicine = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.JYBZ.getType(), map.get("basicMedicineCode").toString());
            if (StringUtils.isEmpty(basicMedicine)) {
                res.put("msg", "基药标识编码错误");
                return res;
            }
            drug.setBasicMedicineCode(map.get("basicMedicineCode").toString());
            drug.setBasicMedicine(basicMedicine);
        }
        if (!StringUtils.isEmpty(map.get("isAntibiotics"))) {
            drug.setIsAntibiotics(map.get("isAntibiotics").toString());
        }
        if (!StringUtils.isEmpty(map.get("isInjection"))) {
            drug.setIsInjection(map.get("isInjection").toString());
        }
        if (!StringUtils.isEmpty(map.get("isAnesthesia"))) {
            drug.setIsAnesthesia(map.get("isAnesthesia").toString());
        }
        if (!StringUtils.isEmpty(map.get("isMonitor"))) {
            drug.setIsMonitor(map.get("isMonitor").toString());
        }
        if (!StringUtils.isEmpty(map.get("psychotropicDrug"))) {
            drug.setPsychotropicDrug(map.get("psychotropicDrug").toString());
        }
        if (!StringUtils.isEmpty(map.get("hospitalPreparations"))) {
            drug.setHospitalPreparations(map.get("hospitalPreparations").toString());
        }
        if (!StringUtils.isEmpty(map.get("content"))) {
            drug.setContent(map.get("content").toString());
        }
        if (!StringUtils.isEmpty(map.get("erp"))) {
            drug.setErp(map.get("erp").toString());
        }
        if (!StringUtils.isEmpty(map.get("erpId"))) {
            drug.setErpId(map.get("erpId").toString());
        }
        if (!StringUtils.isEmpty(map.get("supplier"))) {
            drug.setSupplier(map.get("supplier").toString());
        }
        this.drugMapper.updateById(drug);
        // 对应的报表数据修改
        DicMedicines dicMedicines = new DicMedicines();
        dicMedicines.setYbmlbm(drug.getHealthCode());
        dicMedicines.setTymc(drug.getName());
        dicMedicines.setJxda(drug.getDrugDosCode());
        dicMedicines.setYppzwh(drug.getApprovalNumber());
        dicMedicines.setBzjx(drug.getDrugDosName());
        dicMedicines.setYnzjbz(drug.getHospitalPreparations());
        dicMedicines.setJybz(drug.getBasicMedicineCode());
        this.dicMedicinesMapper.update(dicMedicines, new QueryWrapper<DicMedicines>().eq("drug_id", drug.getId()));
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "修改药品");
            }
        });
        return res;
    }

    @Override
    public void drugBulkExport(String name, String manufacturers, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        if (null != name && !"".equals(name)) {
            wrapper.like("name", name);
        }
        if (null != manufacturers && !"".equals(manufacturers)) {
            wrapper.like("manufacturers", manufacturers);
        }
        List<Drug> drugs = this.drugMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(drugs)) {
            throw new BizException("当前条件下没有药品数据");
        }
        List<String> ids = drugs.stream().map(Drug::getCreateUserId).distinct().collect(Collectors.toList());
        List<SysUser> users = this.sysUserMapper.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(drugs)) {
            throw new BizException("根据用户的id没有查询到用户数据 id： " + ids);
        }
        Map<String, String> userMap = users.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getUserName));
        List<DrugEntity> drugEntities = new ArrayList<>();
        drugs.forEach(drug -> {
            DrugEntity entity = new DrugEntity();
            if ("1".equals(drug.getIsAntibiotics())) {
                drug.setIsAntibiotics("是");
            } else {
                drug.setIsAntibiotics("否");
            }
            if ("1".equals(drug.getIsInjection())) {
                drug.setIsInjection("是");
            } else {
                drug.setIsInjection("否");
            }
            if ("1".equals(drug.getIsAnesthesia())) {
                drug.setIsAnesthesia("是");
            } else {
                drug.setIsAnesthesia("否");
            }
            if ("1".equals(drug.getIsMonitor())) {
                drug.setIsMonitor("是");
            } else {
                drug.setIsMonitor("否");
            }
            if ("1".equals(drug.getPsychotropicDrug())) {
                drug.setPsychotropicDrug("有");
            } else {
                drug.setPsychotropicDrug("无");
            }
            if ("1".equals(drug.getHospitalPreparations())) {
                drug.setHospitalPreparations("是");
            } else {
                drug.setHospitalPreparations("否");
            }
            if ("1".equals(drug.getStatus())) {
                drug.setStatus("启用");
            } else {
                drug.setStatus("停用");
            }
            if ("1".equals(drug.getIsBasicMedicine())) {
                drug.setIsBasicMedicine("是");
            } else {
                drug.setIsBasicMedicine("否");
            }
            BeanUtils.copyProperties(drug, entity);
            entity.setCreateUserName(userMap.get(drug.getCreateUserId()));
            drugEntities.add(entity);
        });
        ExcelUtils.writeExcel(request, response, drugEntities, DrugEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "药品数据表格.xlsx");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONArray drugBulkImport(List<DrugEntity> drugEntities, HttpServletRequest request) {
        JSONArray res = new JSONArray();
        // 验证数据准备
        List<Dict> drugDosDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.DRUG_DOS.getType()));
        Map<String, String> drugDosMap = drugDosDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> packUnitDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.PACK_UNIT.getType()));
        Map<String, String> packUnitMap = packUnitDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> freqMedDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.FREQ_MED.getType()));
        Map<String, String> freqMedMap = freqMedDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> routeAdmiDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.ROUTE_ADMI.getType()));
        Map<String, String> routeAdmiMap = routeAdmiDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> jybsDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.JYBZ.getType()));
        Map<String, String> jybsMap = jybsDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        List<Dict> doseUnitDicts = this.diceMapper.selectList(new QueryWrapper<Dict>().eq("type", DictTypeEnum.MED_UNIT.getType()));
        Map<String, String> doseUnitDictMap = doseUnitDicts.stream().collect(Collectors.toMap(Dict::getName, Dict::getCode));

        // 错误标记
        boolean flag = false;
        for (int i = 0; i < drugEntities.size(); i++) {
            DrugEntity drugEntity = drugEntities.get(i);
            /*if (StringUtils.isEmpty(drugEntity.getSkuCode())) {
                res.add("商品编码: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }*/
            /*if (StringUtils.isEmpty(drugEntity.getMshId())) {
                res.add("合偶平方ID: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }*/
            if (StringUtils.isEmpty(drugEntity.getHealthCode())) {
                res.add("阳光码: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (StringUtils.isEmpty(drugEntity.getName())) {
                res.add("药品名称: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (StringUtils.isEmpty(drugEntity.getSize())) {
                res.add("药品规格: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (StringUtils.isEmpty(drugEntity.getDrugDosName())) {
                res.add("药物剂型: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                if (!drugDosMap.containsKey(drugEntity.getDrugDosName())) {
                    res.add("药物剂型: 第" + (i + 1) + "行" + "不存在");
                    flag = true;
                } else {
                    drugEntity.setDrugDosCode(drugDosMap.get(drugEntity.getDrugDosName()));
                }
            }
            if (StringUtils.isEmpty(drugEntity.getUnitMeasure())) {
                res.add("单位剂量: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (StringUtils.isEmpty(drugEntity.getDoseUnit())) {
                res.add("剂量单位: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                if (!doseUnitDictMap.containsKey(drugEntity.getDoseUnit())) {
                    res.add("剂量单位: 第" + (i + 1) + "行" + "不存在");
                    flag = true;
                } else {
                    drugEntity.setDoseUnitCode(doseUnitDictMap.get(drugEntity.getDoseUnit()));
                }
            }
            if (StringUtils.isEmpty(drugEntity.getManufacturers())) {
                res.add("厂家: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (StringUtils.isEmpty(drugEntity.getApprovalNumber())) {
                res.add("批准文号: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (StringUtils.isEmpty(drugEntity.getPackUnitName())) {
                res.add("包装单位: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                if (!packUnitMap.containsKey(drugEntity.getPackUnitName())) {
                    res.add("包装单位: 第" + (i + 1) + "行" + "不存在");
                    flag = true;
                } else {
                    drugEntity.setPackUnitCode(packUnitMap.get(drugEntity.getPackUnitName()));
                }
            }
            if (StringUtils.isEmpty(drugEntity.getPrice())) {
                res.add("药品单价: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
            if (!StringUtils.isEmpty(drugEntity.getFreqMedName())) {
                if (!freqMedMap.containsKey(drugEntity.getFreqMedName())) {
                    res.add("默认用药频次: 第" + (i + 1) + "行" + "不存在");
                    flag = true;
                } else {
                    drugEntity.setFreqMedCode(freqMedMap.get(drugEntity.getFreqMedName()));
                }
            }
            if (!StringUtils.isEmpty(drugEntity.getRouteAdmiName())) {
                if (!routeAdmiMap.containsKey(drugEntity.getRouteAdmiName())) {
                    res.add("默认用药途径: 第" + (i + 1) + "行" + "不存在");
                    flag = true;
                } else {
                    drugEntity.setRouteAdmiCode(routeAdmiMap.get(drugEntity.getRouteAdmiName()));
                }
            }
            if (StringUtils.isEmpty(drugEntity.getIsBasicMedicine())) {
                res.add("是否基药: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setIsBasicMedicine(drugEntity.getIsBasicMedicine().equals("是") ? "1" : "0");
            }
            if (StringUtils.isEmpty(drugEntity.getBasicMedicine())) {
                res.add("基药标识: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                if (!jybsMap.containsKey(drugEntity.getBasicMedicine())) {
                    res.add("基药标识: 第" + (i + 1) + "行" + "不存在");
                    flag = true;
                } else {
                    String basicMedicine = jybsMap.get(drugEntity.getBasicMedicine());
                    if ("0".equals(basicMedicine)) {
                        //0为非前述两种，则  是否基药为否
                        if (!"0".equals(drugEntity.getIsBasicMedicine())) {
                            res.add("是否基药: 第" + (i + 1) + "行" +
                                    "与基药标识编码没有对应！当是否基药字段为否，那么基药标识里面就是非前述两种；当是否基药字段为是，那么基药标识里面就是国基药或增补基药");
                            flag = true;
                        }
                    } else {
                        // 则  是否基药为是
                        if (!"1".equals(drugEntity.getIsBasicMedicine())) {
                            res.add("是否基药: 第" + (i + 1) + "行" +
                                    "与基药标识编码没有对应！当是否基药字段为否，那么基药标识里面就是非前述两种；当是否基药字段为是，那么基药标识里面就是国基药或增补基药");
                            flag = true;
                        }
                    }
                    drugEntity.setBasicMedicineCode(basicMedicine);
                }
            }
            if (StringUtils.isEmpty(drugEntity.getIsAntibiotics())) {
                res.add("是否抗生素: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setIsAntibiotics(drugEntity.getIsAntibiotics().equals("是") ? "1" : "0");
            }
            if (StringUtils.isEmpty(drugEntity.getIsInjection())) {
                res.add("是否注射: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setIsInjection(drugEntity.getIsInjection().equals("是") ? "1" : "0");
            }
            if (StringUtils.isEmpty(drugEntity.getIsAnesthesia())) {
                res.add("是否麻醉药: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setIsAnesthesia(drugEntity.getIsAnesthesia().equals("是") ? "1" : "0");
            }
            if (StringUtils.isEmpty(drugEntity.getIsMonitor())) {
                res.add("是否监控药物: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setIsMonitor(drugEntity.getIsMonitor().equals("是") ? "1" : "0");
            }
            if (StringUtils.isEmpty(drugEntity.getPsychotropicDrug())) {
                res.add("精神药物级别: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setPsychotropicDrug(drugEntity.getPsychotropicDrug().equals("有") ? "1" : "0");
            }
            if (StringUtils.isEmpty(drugEntity.getHospitalPreparations())) {
                res.add("是否院内制剂: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            } else {
                drugEntity.setHospitalPreparations(drugEntity.getHospitalPreparations().equals("是") ? "1" : "0");
            }
            /*if (StringUtils.isEmpty(drugEntity.getContent())) {
                res.add("药品说明书: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }*/
//            if (StringUtils.isEmpty(drugEntity.getErp())) {
//                res.add("ERP码: 第" + (i + 1) + "行" + "不能为空");
//                flag = true;
//            }
//            if (StringUtils.isEmpty(drugEntity.getErpId())) {
//                res.add("ERPID: 第" + (i + 1) + "行" + "不能为空");
//                flag = true;
//            }
            if (StringUtils.isEmpty(drugEntity.getSupplier())) {
                res.add("供应商: 第" + (i + 1) + "行" + "不能为空");
                flag = true;
            }
        }
        if (flag) {
            return res;
        } else {
            // 批量导入是否存在已有的药品
            List<String> ids = drugEntities.stream().map(DrugEntity::getHealthCode).collect(Collectors.toList());
            List<Drug> drugs = this.drugMapper.selectList(new QueryWrapper<Drug>().in("health_code", ids));
            if (!CollectionUtils.isEmpty(drugs)) {
                throw new BizException("本地提交药品在系统已存在 药品的代码 ：" + drugs.stream().map(Drug::getHealthCode).collect(Collectors.toList()));
            }
            List<DicMedicines> dicMedicines = new ArrayList<>();
            drugEntities.forEach(s -> {
                Drug drug = new Drug();
                drug.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
                BeanUtils.copyProperties(s, drug);
                drugMapper.insert(drug);
                // 插入系统规则的名仕汇id
                Drug mshIdDrug = new Drug();
                mshIdDrug.setId(drug.getId());
                mshIdDrug.setMshId(IdUtils.getDrugID(Integer.valueOf(drug.getId())));
                drugMapper.updateById(mshIdDrug);
                DicMedicines dicMedicine = new DicMedicines();
                dicMedicine.setDrugId(drug.getId());
                dicMedicine.setYljgdm(ReportCodingEnum.YLJGDM);
                dicMedicine.setYbmlbm(drug.getHealthCode());
                dicMedicine.setWsjgdm(ReportCodingEnum.WSJGDM);
                dicMedicine.setSybz("1");
                dicMedicine.setTymc(drug.getName());
                dicMedicine.setJxda(drug.getDrugDosCode());
                dicMedicine.setYppzwh(drug.getApprovalNumber());
                dicMedicine.setBzjx(drug.getDrugDosName());
                dicMedicine.setYnzjbz(drug.getHospitalPreparations());
                dicMedicine.setJybz(drug.getBasicMedicineCode());
                dicMedicine.setKssbz(ReportCodingEnum.KSSBZ);
                dicMedicine.setDmjfbs(ReportCodingEnum.DMJFBS);
                dicMedicine.setXgbz(ReportCodingEnum.XGBZ);
                dicMedicines.add(dicMedicine);
            });
            // 对应上报数据批量写入
            this.dicMedicinesService.saveBatch(dicMedicines);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                public void afterCommit() {
                    loginRecordUtil.saveLoginRecord(request, "药品批量导入");
                }
            });
            return res;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject insert(Map<String, Object> map, HttpServletRequest request) {
        BizException exception = new BizException();
        JSONObject res = new JSONObject();
        /*if (null == map.get("skuCode") || "".equals(map.get("skuCode").toString())) {
            exception.setMessage("商品编码必填");
            throw exception;
        }else {
            Drug drug = this.drugService.getOne(new QueryWrapper<Drug>().eq("sku_code", map.get("skuCode")));
            if (!StringUtils.isEmpty(drug)) {
                exception.setMessage("已存在该药物请勿重复添加 重复药物代码：" + map.get("skuCode"));
                throw exception;
            }
        }*/
        /*if (null == map.get("mshId") || "".equals(map.get("mshId"))) {
            exception.setMessage("合偶平方ID必填");
            throw exception;
        }*/
        if (null == map.get("healthCode") || "".equals(map.get("healthCode"))) {
            exception.setMessage("药品代码（阳光码）必填");
            throw exception;
        } else {
            Drug drug = this.drugMapper.selectOne(new QueryWrapper<Drug>().eq("health_code", map.get("healthCode")).eq("status", 1));
            if (!StringUtils.isEmpty(drug)) {
                exception.setMessage("已存在该药物请勿重复添加 重复药物代码：" + map.get("healthCode"));
                throw exception;
            }
        }
        if (null == map.get("name") || "".equals(map.get("name"))) {
            exception.setMessage("药品名称必填");
            throw exception;
        }
        if (null == map.get("size") || "".equals(map.get("size"))) {
            exception.setMessage("药品规格必填");
            throw exception;
        }
        if (null == map.get("drugDosCode") || "".equals(map.get("drugDosCode"))) {
            exception.setMessage("药品剂型编码必填");
            throw exception;
        }
        if (null == map.get("unitMeasure") || "".equals(map.get("unitMeasure"))) {
            exception.setMessage("单位剂量必填");
            throw exception;
        }
        if (null == map.get("doseUnitCode") || "".equals(map.get("doseUnitCode"))) {
            exception.setMessage("剂量单位编码必填");
            throw exception;
        }
        if (null == map.get("manufacturers") || "".equals(map.get("manufacturers"))) {
            exception.setMessage("厂家名称必填");
            throw exception;
        }
        if (null == map.get("approvalNumber") || "".equals(map.get("approvalNumber"))) {
            exception.setMessage("批准文号必填");
            throw exception;
        }
        if (null == map.get("packUnitCode") || "".equals(map.get("packUnitCode"))) {
            exception.setMessage("包装单位编码必填");
            throw exception;
        }
        if (null == map.get("price") || "".equals(map.get("price"))) {
            exception.setMessage("药品单价必填");
            throw exception;
        }
        /*if (null == map.get("freqMedCode") || "".equals(map.get("freqMedCode"))) {
            exception.setMessage("默认用药频次编码必填");
            throw exception;
        }*/
        /*if (null == map.get("routeAdmiCode") || "".equals(map.get("routeAdmiCode"))) {
            exception.setMessage("默认用药途径编码必填");
            throw exception;
        }*/
        if (null == map.get("isBasicMedicine") || "".equals(map.get("isBasicMedicine"))) {
            exception.setMessage("是否基药必填");
            throw exception;
        }
        if (null == map.get("basicMedicineCode") || "".equals(map.get("basicMedicineCode"))) {
            exception.setMessage("基药标识编码必填");
            throw exception;
        }
        if (null == map.get("isAntibiotics") || "".equals(map.get("isAntibiotics"))) {
            exception.setMessage("是否抗生素必填");
            throw exception;
        }
        if (null == map.get("isInjection") || "".equals(map.get("isInjection"))) {
            exception.setMessage("是否注射必填");
            throw exception;
        }
        if (null == map.get("isAnesthesia") || "".equals(map.get("isAnesthesia"))) {
            exception.setMessage("是否麻醉药必填");
            throw exception;
        }
        if (null == map.get("isMonitor") || "".equals(map.get("isMonitor"))) {
            exception.setMessage("是否监控药物必填");
            throw exception;
        }
        if (null == map.get("psychotropicDrug") || "".equals(map.get("psychotropicDrug"))) {
            exception.setMessage("精神药物级别必填");
            throw exception;
        }
        if (null == map.get("hospitalPreparations") || "".equals(map.get("hospitalPreparations"))) {
            exception.setMessage("是否院内制剂必填");
            throw exception;
        }
        /*if (null == map.get("content") || "".equals(map.get("content"))) {
            exception.setMessage("药品说明书必填");
            throw exception;
        }
        if (null == map.get("erp") || "".equals(map.get("erp"))) {
            exception.setMessage("ERP码必填");
            throw exception;
        }
        if (null == map.get("erpId") || "".equals(map.get("erpId"))) {
            exception.setMessage("ERPID必填");
            throw exception;
        }*/
        String drugDosName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.DRUG_DOS.getType(), map.get("drugDosCode").toString());
        if (StringUtils.isEmpty(drugDosName)) {
            exception.setMessage("药物剂型编码错误");
            throw exception;
        }
        String packUnitName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.PACK_UNIT.getType(), map.get("packUnitCode").toString());
        if (StringUtils.isEmpty(packUnitName)) {
            exception.setMessage("包装单位编码错误");
            throw exception;
        }
        String doseUnitName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.MED_UNIT.getType(), map.get("doseUnitCode").toString());
        if (StringUtils.isEmpty(doseUnitName)) {
            exception.setMessage("剂量单位编码错误");
            throw exception;
        }
        if (null != map.get("freqMedCode") && !"".equals(map.get("freqMedCode").toString())) {
            String freqMedName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.FREQ_MED.getType(), map.get("freqMedCode").toString());
            if (StringUtils.isEmpty(freqMedName)) {
                exception.setMessage("默认用药频次编码错误");
                throw exception;
            }
            map.put("freqMedName", freqMedName);
        }
        if (null != map.get("routeAdmiCode") && !"".equals(map.get("routeAdmiCode").toString())) {
            String routeAdmiName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.ROUTE_ADMI.getType(), map.get("routeAdmiCode").toString());
            if (StringUtils.isEmpty(routeAdmiName)) {
                exception.setMessage("默认用药途径编码错误");
                throw exception;
            }
            map.put("routeAdmiName", routeAdmiName);
        }

        String basicMedicineName = diceService.getDoctorNameByTypeAndCode(DictTypeEnum.JYBZ.getType(), map.get("basicMedicineCode").toString());
        if (StringUtils.isEmpty(basicMedicineName)) {
            exception.setMessage("基药标识编码错误");
            throw exception;
        }
        if (null == map.get("supplier") || "".equals(map.get("supplier").toString())) {
            exception.setMessage("药品的供应商必填");
            throw exception;
        }

        map.put("drugDosName", drugDosName);
        map.put("packUnitName", packUnitName);
        map.put("basicMedicineName", basicMedicineName);
        Drug drug = new Drug();
        drug.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        /*drug.setSkuCode(map.get("skuCode").toString().trim());*/
        /*drug.setMshId(map.get("mshId").toString().trim());*/
        drug.setHealthCode(map.get("healthCode").toString().trim());
        drug.setName(map.get("name").toString().trim());
        drug.setSize(map.get("size").toString().trim());
        drug.setDrugDosCode(map.get("drugDosCode").toString().trim());
        drug.setDrugDosName(map.get("drugDosName").toString().trim());
        drug.setUnitMeasure(map.get("unitMeasure").toString().trim());
        drug.setDoseUnit(doseUnitName);
        drug.setDoseUnitCode(map.get("doseUnitCode").toString().trim());
        drug.setManufacturers(map.get("manufacturers").toString().trim());
        drug.setApprovalNumber(map.get("approvalNumber").toString().trim());
        drug.setPackUnitCode(map.get("packUnitCode").toString().trim());
        drug.setPackUnitName(map.get("packUnitName").toString().trim());
        drug.setPrice(map.get("price").toString().trim());
        if (null != map.get("freqMedCode") && !"".equals(map.get("freqMedCode").toString())) {
            drug.setFreqMedCode(map.get("freqMedCode").toString().trim());
            drug.setFreqMedName(map.get("freqMedName").toString().trim());
        }
        if (null != map.get("routeAdmiCode") && !"".equals(map.get("routeAdmiCode").toString())) {
            drug.setRouteAdmiCode(map.get("routeAdmiCode").toString().trim());
            drug.setRouteAdmiName(map.get("routeAdmiName").toString().trim());
        }

        drug.setIsBasicMedicine(map.get("isBasicMedicine").toString().trim());
        drug.setBasicMedicineCode(map.get("basicMedicineCode").toString().trim());
        drug.setBasicMedicine(map.get("basicMedicineName").toString().trim());
        drug.setIsAntibiotics(map.get("isAntibiotics").toString().trim());
        drug.setIsInjection(map.get("isInjection").toString().trim());
        drug.setIsAnesthesia(map.get("isAnesthesia").toString().trim());
        drug.setIsMonitor(map.get("isMonitor").toString().trim());
        drug.setPsychotropicDrug(map.get("psychotropicDrug").toString().trim());
        drug.setHospitalPreparations(map.get("hospitalPreparations").toString().trim());
        drug.setSupplier(map.get("supplier").toString().trim());
        if (null != map.get("content") && !"".equals(map.get("content").toString())) {
            drug.setContent(map.get("content").toString().trim());
        } else {
            drug.setContent(null);
        }
        if (null != map.get("erp") && !"".equals(map.get("erp").toString())) {
            drug.setErp(map.get("erp").toString().trim());
        } else {
            drug.setErp(null);
        }
        if (null != map.get("erpId") && !"".equals(map.get("erpId").toString())) {
            drug.setErpId(map.get("erpId").toString().trim());
        } else {
            drug.setErpId(null);
        }
        this.drugMapper.insert(drug);
        // 写入系统规则的名仕汇id
        Drug drugNum = new Drug();
        drugNum.setId(drug.getId());
        drugNum.setMshId(IdUtils.getDrugID(Integer.valueOf(drug.getId())));
        this.drugMapper.updateById(drugNum);
        // 写入上报数据表
        DicMedicines dicMedicines = new DicMedicines();
        dicMedicines.setDrugId(drug.getId());
        dicMedicines.setYljgdm(ReportCodingEnum.YLJGDM);
        dicMedicines.setYbmlbm(drug.getHealthCode());
        dicMedicines.setWsjgdm(ReportCodingEnum.WSJGDM);
        dicMedicines.setSybz("1");
        dicMedicines.setTymc(drug.getName());
        dicMedicines.setJxda(drug.getDrugDosCode());
        dicMedicines.setYppzwh(drug.getApprovalNumber());
        dicMedicines.setBzjx(drug.getDrugDosName());
        dicMedicines.setYnzjbz(drug.getHospitalPreparations());
        dicMedicines.setJybz(drug.getBasicMedicineCode());
        dicMedicines.setKssbz(ReportCodingEnum.KSSBZ);
        dicMedicines.setDmjfbs(ReportCodingEnum.DMJFBS);
        dicMedicines.setXgbz(ReportCodingEnum.XGBZ);
        this.dicMedicinesMapper.insert(dicMedicines);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            public void afterCommit() {
                loginRecordUtil.saveLoginRecord(request, "添加药品");
            }
        });
        return res;
    }

    @Override
    public PageResult<DrugVo> getPage(PageParam<Drug> pageParam, SearchDrugRequest request) {

        LambdaQueryWrapper<Drug> queryWrapper = new LambdaQueryWrapper<Drug>()
                .like(StringUtil.isNotBlank(request.getName()), Drug::getName, request.getName())
                .eq(StringUtil.isNotBlank(request.getDrugstoreId()), Drug::getDrugstoreId, request.getDrugstoreId())
                .likeRight(StringUtil.isNotBlank(request.getInitial()), Drug::getPinyinInitial, request.getInitial().toUpperCase())
                .orderByDesc(Drug::getCreateTime);

        // 执行分页查询
        Page<Drug> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }
        List<String> drugstoreIds = page.getRecords().stream().map(Drug::getDrugstoreId).distinct().toList();
        Map<String, String> drugstoreMap = drugstoreService.listByIds(drugstoreIds)
                .stream().collect(Collectors.toMap(Drugstore::getId, Drugstore::getName));
        List<DrugVo> voList = new ArrayList<>();
        for (Drug drug : page.getRecords()) {
            DrugVo vo = new DrugVo();
            BeanUtils.copyProperties(drug, vo);
            vo.setDrugstoreName(drugstoreMap.get(vo.getDrugstoreId()));
            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

    @Override
    public boolean create(SaveUpdateDrugRequest request) {
        String drugstoreId = request.getDrugstoreId();
        Drugstore drugstore = drugstoreService.getById(drugstoreId);
        if (drugstore == null) {
            return false;
        }
        Drug drug = new Drug();
        BeanUtils.copyProperties(request, drug);
        return save(drug);
    }

    @Override
    public boolean update(SaveUpdateDrugRequest request) {
        Drug drug = new Drug();
        BeanUtils.copyProperties(request, drug);
        return updateById(drug);
    }

    @Override
    public boolean delete(String id) {
        return removeById(id);
    }

    @Override
    public boolean updateStatus(String id, String status) {
        Drug drug = new Drug();
        drug.setId(id);
        drug.setStatus(status);
        return updateById(drug);
    }
}
