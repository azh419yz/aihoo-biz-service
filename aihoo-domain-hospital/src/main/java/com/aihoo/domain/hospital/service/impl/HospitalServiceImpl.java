package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.constant.DictTypeEnum;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.doctor.service.DoctorUserService;
import com.aihoo.domain.hospital.model.entity.Department;
import com.aihoo.domain.hospital.model.entity.Hospital;
import com.aihoo.domain.hospital.model.entity.HospitalDepartment;
import com.aihoo.domain.hospital.model.excel.HospitalEntity;
import com.aihoo.domain.hospital.model.mapper.DepartmentMapper;
import com.aihoo.domain.hospital.model.mapper.HospitalDepartmentMapper;
import com.aihoo.domain.hospital.model.mapper.HospitalMapper;
import com.aihoo.domain.hospital.model.vo.HospitalDetailsVo;
import com.aihoo.domain.hospital.model.vo.HospitalListVo;
import com.aihoo.domain.hospital.model.vo.HospitalPageVo;
import com.aihoo.domain.hospital.model.vo.HospitalVo;
import com.aihoo.domain.hospital.service.DepartmentService;
import com.aihoo.domain.hospital.service.HospitalDepartmentService;
import com.aihoo.domain.hospital.service.HospitalService;
import com.aihoo.domain.sys.model.entity.Area;
import com.aihoo.domain.sys.model.mapper.AreaMapper;
import com.aihoo.domain.sys.service.AreaService;
import com.aihoo.domain.sys.service.DiceService;
import com.aihoo.domain.sys.util.LoginRecordUtil;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.util.IdUtils;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.SecurityUtils;
import com.aihoo.util.StringHandler;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Classname HospitalServiceImpl
 * @Description hf
 * @Date 2020/9/16 20:43
 * @Created by ad
 */
@Service
@Slf4j
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    @Resource
    private HospitalMapper hospitalMapper;

    @Resource
    private HospitalDepartmentMapper hospitalDepartmentMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private AreaMapper areaMapper;
    @Resource
    private DoctorUserService doctorUserService;
    @Resource
    private DiceService diceService;
    @Resource
    private AreaService areaService;

    @Resource
    private DoctorUserMapper doctorUserMapper;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private HospitalDepartmentService hospitalDepartmentService;

    @Resource
    private LoginRecordUtil loginRecordUtil;

    @Override
    public PageResult<HospitalPageVo> list(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<Hospital> wrapper = new QueryWrapper<>();
        if (null != map.get("hosName") && !"".equals(map.get("hosName").toString())) {
            wrapper.like("hos_name", map.get("hosName"));
        }
        if (null != map.get("hosGradeCode") && !"".equals(map.get("hosGradeCode").toString())) {
            wrapper.eq("hos_grade_code", map.get("hosGradeCode"));
        }
        if (null != map.get("hosCateCode") && !"".equals(map.get("hosCateCode").toString())) {
            wrapper.eq("hos_cate_code", map.get("hosCateCode"));
        }
        if (null != map.get("hosLevelCode") && !"".equals(map.get("hosLevelCode").toString())) {
            wrapper.eq("hos_level_code", map.get("hosLevelCode"));
        }
        wrapper.eq("is_delete", 0);
        if (null != map.get("hosAttCode") && !"".equals(map.get("hosAttCode").toString())) {
            wrapper.eq("hos_att_code", map.get("hosAttCode"));
        }
        wrapper.orderByDesc("create_time");
        IPage<Hospital> iPage = hospitalMapper.selectPage(new Page<>(page, limit), wrapper);
        //查询关联对应关联的科室
        List<Hospital> records = iPage.getRecords();
        //过滤已删除数据
        List<HospitalPageVo> hospitalPageVos = records.stream().map(r -> {
            HospitalPageVo hospitalPageVo = new HospitalPageVo();
            BeanUtils.copyProperties(r, hospitalPageVo);
            return hospitalPageVo;
        }).collect(Collectors.toList());
        List<String> hospitalIds = hospitalPageVos.stream().map(HospitalPageVo::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hospitalIds)) {
            return new PageResult<>(hospitalPageVos, iPage.getTotal());
        }
        //查询医生数量
        List<DoctorUser> doctorUsers = doctorUserMapper.findByDoctorUserIds(hospitalIds);
        Map<String, List<DoctorUser>> listMap = doctorUsers.stream().filter(v -> v.getIsCancel() != "1")
                .collect(Collectors.groupingBy(DoctorUser::getHospitalId));
        hospitalPageVos.forEach(res -> {
            if (!StringUtils.isEmpty(listMap.get(res.getId()))) {
                long count = listMap.get(res.getId()).stream().count();
                res.setDoctorCount(count);
            }else {
                res.setDoctorCount(0l);
            }
        });
        return new PageResult<>(hospitalPageVos, iPage.getTotal());
    }

    @Override
    public JSONObject controlHospital(String id) {
        Hospital hospitals = this.hospitalMapper.selectOne(new QueryWrapper<Hospital>().eq("id", id));
        if (null == hospitals) {
            log.error("根据医院id未查询到医院 id为{}", id);
            throw new BizException("根据医院id未查询到医院");
        }
        HospitalDetailsVo hospitalDetailsVo = new HospitalDetailsVo();
        BeanUtils.copyProperties(hospitals, hospitalDetailsVo);
        //查询医生数量
        Map<String,String> params = new HashMap<>();
        params.put("hospitalId",id);
        List<DoctorUser> doctorUsers = doctorUserMapper.findByObject(params);
        if (!StringUtils.isEmpty(doctorUsers)) {
            long count = doctorUsers.stream().count();
            hospitalDetailsVo.setDoctorCount(count);
        }else {
            hospitalDetailsVo.setDoctorCount(0l);
        }
        List<Department> departments = this.departmentMapper.
                selectList(new QueryWrapper<Department>().eq("status", 1));
        if (CollectionUtils.isEmpty(departments)) {
            log.error("状态为启用的科室不存在");
            throw new NullPointerException();
        }
        //根据 level 分组
        Map<String, List<Department>> map = departments.stream().collect(Collectors.groupingBy(Department::getLevel));
        List<Department> firstLevel = null;
        List<Department> secondLevel = null;
        for (Map.Entry<String, List<Department>> entry : map.entrySet()) {
            //所有一级科室
            if ("1".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            //所有二级科室
            if ("2".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
        }
        assert secondLevel != null;
        assert firstLevel != null;
        Map<String, List<Department>> collect = secondLevel.stream().collect(Collectors.groupingBy(Department::getParentCode));
        JSONArray jsonArray = new JSONArray();
        //查询下级科室
        List<HospitalDepartment> departmentList = this.hospitalDepartmentMapper.
                selectList(new QueryWrapper<HospitalDepartment>().eq("hospital_id", id));
        if (CollectionUtils.isEmpty(departmentList)) {
            log.error("根据医院的id未查询到医院详情 id为{}", id);
            throw new BizException("根据医院的id未查询到医院详情");
        }
        List<String> codeList = departmentList.stream().map(HospitalDepartment::getDepartCode).collect(Collectors.toList());
        firstLevel.forEach(department -> {
            JSONArray secondJsonArray = new JSONArray();
            JSONObject resJson = new JSONObject();
            resJson.put("code", department.getCode());
            resJson.put("title", department.getName());
            if (codeList.contains(department.getCode())) {
                resJson.put("isSelect", "1");
                resJson.put("expand", true);
            }else {
                resJson.put("isSelect", "0");
            }
            //该一级对应的所有二级科室 可能存在一级下面不存在二级科室
            List<Department> list = collect.get(department.getCode());
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(s -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", s.getCode());
                    jsonObject.put("title", s.getName());
                    if (codeList.contains(s.getCode())) {
                        jsonObject.put("isSelect", "1");
                        jsonObject.put("checked", true);
                    }else {
                        jsonObject.put("isSelect", "0");
                    }
                    secondJsonArray.add(jsonObject);
                });
                resJson.put("children", secondJsonArray);
            }
            jsonArray.add(resJson);
        });
        hospitalDetailsVo.setDepartCodeArray(jsonArray);
        JSONObject resJson = JSONObject.parseObject(JSON.toJSON(hospitalDetailsVo).toString());
        return resJson;
    }

    @Override
    public JSONArray departmentRelation() {
        List<Department> departments = this.departmentMapper.
                selectList(new QueryWrapper<Department>().eq("status", 1));
        if (CollectionUtils.isEmpty(departments)) {
            log.error("状态为启用的科室不存在");
            throw new NullPointerException();
        }
        //根据 level 分组
        Map<String, List<Department>> map = departments.stream().collect(Collectors.groupingBy(Department::getLevel));
        List<Department> firstLevel = null;
        List<Department> secondLevel = null;

        for (Map.Entry<String, List<Department>> entry : map.entrySet()) {
            //所有一级科室
            if ("1".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            //所有二级科室
            if ("2".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
        }
        assert secondLevel != null;
        assert firstLevel != null;
        Map<String, List<Department>> collect = secondLevel.stream().collect(Collectors.groupingBy(Department::getParentCode));
        JSONArray jsonArray = new JSONArray();
        firstLevel.forEach(department -> {
            JSONArray secondJsonArray = new JSONArray();
            JSONObject resJson = new JSONObject();
            resJson.put("code", department.getCode());
            resJson.put("title", department.getName());
            resJson.put("name", department.getName());
            //该一级对应的所有二级科室 可能存在一级下面不存在二级科室
            List<Department> list = collect.get(department.getCode());
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(s -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", s.getCode());
                    jsonObject.put("title", s.getName());
                    jsonObject.put("name", s.getName());
                    secondJsonArray.add(jsonObject);
                });
                resJson.put("children", secondJsonArray);
                resJson.put("secondDepartment", secondJsonArray);
            }
            jsonArray.add(resJson);
        });
        return jsonArray;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject hospitalUpdate(Map<String, Object> map,HttpServletRequest request) {
        JSONObject res = new JSONObject();
        if (null == map.get("id") || "".equals(map.get("id"))) {
            res.put("msg","请携带医院id");
            return res;
        }
        Hospital hospital = new Hospital();
        hospital.setId(map.get("id").toString().trim());
        if (null != map.get("hosName") && !"".equals(map.get("hosName"))) {
            //判断医院名称是否存在，已存在不允许修改
            Hospital byId = hospitalMapper.selectById(map.get("id").toString());
            if (byId.getHosName().equals(map.get("hosName"))) {
                hospital.setHosName(map.get("hosName").toString().trim());
            }else {
                List<Hospital> selectList = hospitalMapper.selectList(new QueryWrapper<Hospital>().eq("is_delete", 0));
                List<String> hospitalList = selectList.stream().map(Hospital::getHosName).collect(Collectors.toList());
                if (hospitalList.contains(map.get("hosName"))) {
                    res.put("msg","医院名称已存在");
                    return res;
                }else {
                    hospital.setHosName(map.get("hosName").toString().trim());
                }
            }
        }
        if (null != map.get("hosMobile") && !"".equals(map.get("hosMobile"))) {
            hospital.setHosMobile(map.get("hosMobile").toString().trim());
        }
        if (null != map.get("hosGradeCode") && !"".equals(map.get("hosGradeCode"))) {
            String gradeName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_GRADE.getType(), map.get("hosGradeCode").toString());
            if (!StringHandler.isNotBlank(gradeName)) {
                res.put("msg","未找到编码对应的医院等级");
                return res;
            }
            hospital.setHosGradeCode(map.get("hosGradeCode").toString().trim());
            hospital.setHosGradeName(gradeName);
        }
        if (null != map.get("hosLevelCode") && !"".equals(map.get("hosLevelCode"))) {
            String levelName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_LEVEL.getType(), map.get("hosLevelCode").toString());
            if (!StringHandler.isNotBlank(levelName)) {
                res.put("msg","未找到编码对应的医院级别");
                return res;
            }
            hospital.setHosLevelCode(map.get("hosLevelCode").toString().trim());
            hospital.setHosLevelName(levelName);
        }
        if (null != map.get("hosCateCode") && !"".equals(map.get("hosCateCode"))) {
            String cateName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_CATE.getType(), map.get("hosCateCode").toString());
            if (!StringHandler.isNotBlank(cateName)) {
                res.put("msg","未找到编码对应的医院类型");
                return res;
            }
            hospital.setHosCateCode(map.get("hosCateCode").toString().trim());
            hospital.setHosCateName(cateName);
        }
        if (null != map.get("hosAttCode") && !"".equals(map.get("hosAttCode"))) {
            String attName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_ATT.getType(), map.get("hosAttCode").toString());
            if (!StringHandler.isNotBlank(attName)) {
                res.put("msg","未找到编码对应的医院属性");
                return res;
            }
            hospital.setHosAttCode(map.get("hosAttCode").toString().trim());
            hospital.setHosAttName(attName);
        }
        if (null != map.get("provinceCode") && !"".equals(map.get("provinceCode"))) {
            String provinceName = this.areaService.getAreaByCode(map.get("provinceCode").toString());
            if (!StringHandler.isNotBlank(provinceName)) {
                res.put("msg","未找到编码对应的省");
                return res;
            }
            hospital.setProvinceCode(map.get("provinceCode").toString().trim());
            hospital.setProvince(provinceName);
        }
        if (null != map.get("cityCode") && !"".equals(map.get("cityCode"))) {
            String cityName = this.areaService.getAreaByCode(map.get("cityCode").toString());
            if (!StringHandler.isNotBlank(cityName)) {
                res.put("msg","未找到编码对应的市");
                return res;
            }
            hospital.setCityCode(map.get("cityCode").toString().trim());
            hospital.setCity(cityName);
        }
        if (null != map.get("districtCode") && !"".equals(map.get("districtCode"))) {
            String districtName = this.areaService.getAreaByCode(map.get("districtCode").toString());
            if (!StringHandler.isNotBlank(districtName)) {
                res.put("msg","未找到编码对应的区");
                return res;
            }
            hospital.setDistrictCode(map.get("districtCode").toString().trim());
            hospital.setDistrict(districtName);
        }
        if (null != map.get("address") && !"".equals(map.get("address"))) {
            hospital.setAddress(map.get("address").toString().trim());
        }
        if (null != map.get("content") && !"".equals(map.get("content"))) {
            hospital.setContent(map.get("content").toString().trim());
        }
        if (null != map.get("imgs") && !"".equals(map.get("imgs"))) {
            hospital.setContent(map.get("imgs").toString().trim());
        }
        // 本次编辑科室的所有  newIds
        Object array = new ArrayList<>();
        if (null != map.get("departCodeArray")){
            array = map.get("departCodeArray");
        }
        List<String> newIds = JSONArray.parseArray(JSON.toJSON(array).toString(), String.class);
        if (null != map.get("departCodeArray") && !"".equals(map.get("departCodeArray")) && !newIds.isEmpty()) {
            // 保存医院
            this.hospitalMapper.updateById(hospital);
            // 科室对应操作
            // 该医院已经存在科室  ids
            List<HospitalDepartment> hospitalDepartments = this.hospitalDepartmentService.findDepartCodeAllByHospitalId(map.get("id").toString());
            List<String> ids = hospitalDepartments.stream().map(HospitalDepartment::getDepartCode).collect(Collectors.toList());
            //要删除的数据
            List<String> updateIds = filterList(ids, newIds);
            //要新增的数据
            List<String> insertIds = filterList(newIds, ids);
            if (updateIds.size() == 0) {//无需删除
                if (insertIds.size() != 0) {//需要新增
                    HospitalDepartment department = new HospitalDepartment();
                    department.setHospitalId(map.get("id").toString());
                    department.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
                    insertIds.forEach(code -> {
                        String name = departmentService.findDepartmentNameByCode(code);
                        department.setDepartCode(code);
                        department.setDepartName(name);
                        hospitalDepartmentMapper.insert(department);
                    });
                }
            } else {//需要删除
                //说明有删除的id，需判断该id是否关联医生
                // List<DoctorUser> doctorUsers = doctorUserService.findDoctorUserByIds(updateIds, map.get("id").toString());
                QueryWrapper<DoctorUser> qw = new QueryWrapper();
                qw.apply("hospital_id = {0} AND is_cancel = 0",map.get("id"));
                qw.apply("id NOT IN(SELECT x.id FROM t_doctor_user x WHERE x.is_cancel = 0 AND x.hospital_id = {0} AND FIND_IN_SET(x.depart_code,{1}))",map.get("id"),String.join(",",newIds));
                List<DoctorUser> doctorUsers = doctorUserService.list(qw);

                if (CollectionUtils.isEmpty(doctorUsers)) {
                    //为空说明没有存在关联,删除对应的id
                    hospitalDepartmentService.deleteByDepartCodes(updateIds,map.get("id").toString());
                    if (insertIds.size() != 0) {//需要新增
                        HospitalDepartment department = new HospitalDepartment();
                        department.setHospitalId(map.get("id").toString());
                        department.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
                        insertIds.forEach(code -> {
                            String name = departmentService.findDepartmentNameByCode(code);
                            department.setDepartCode(code);
                            department.setDepartName(name);
                            hospitalDepartmentMapper.insert(department);
                        });
                    }
                    return res;
                } else {
                    //不为空说明有已存在关联的医生，此时本次更新失败
                    JSONArray jsonArray = new JSONArray();
                    doctorUsers.forEach(doctorUser -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(doctorUser.getDepartName(),doctorUser.getName());
                        jsonArray.add(jsonObject);
                    });
                    res.put("msg","本次更新失败，存在以关联医生的科室");
                    res.put("data",jsonArray);
                    return res;
                }
            }
        }else {
            res.put("msg","未携带所属科室code");
            return res;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit(){
                loginRecordUtil.saveLoginRecord(request,"更新医院详情");
            }
        });
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject hospitalInsert(Map<String, Object> map,HttpServletRequest request){
        JSONObject res = new JSONObject();
        if (!StringHandler.isNotBlank("departCodeArray")){
            res.put("msg","请输入医院所属科室code");
            return res;
        }
        List<String> list = JSONArray.parseArray(JSON.toJSON(map.get("departCodeArray")).toString(), String.class);
        if (!StringHandler.isNotBlank("hosName")) {
            res.put("msg","请输入医院名称");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("hosGradeCode")))) {
            res.put("msg","请输入医院等级编码");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("hosLevelCode")))) {
            res.put("msg","请输入医院级别编码");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("provinceCode")))) {
            res.put("msg","请输入省code");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("cityCode")))) {
            res.put("msg","请输入市code");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("districtCode")))) {
            res.put("msg","请输入区code");
            return res;
        } else if (!StringHandler.isNotBlank(String.valueOf(map.get("address")))) {
            res.put("msg","请输入医院详情地址");
            return res;
        } else if (CollectionUtils.isEmpty(list)) {
            res.put("msg","请输入医院所属科室code");
            return res;
        }
        List<Hospital> selectList = hospitalMapper.selectList(new QueryWrapper<Hospital>().eq("is_delete", 0));
        List<String> hospitalList = selectList.stream().map(Hospital::getHosName).collect(Collectors.toList());
        if (hospitalList.contains(map.get("hosName").toString())) {
            res.put("msg","医院名称已存在");
            return res;
        }
        Hospital hospital = new Hospital();
        //必填参数
        hospital.setHosName(map.get("hosName").toString().trim());
        String gradeName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_GRADE.getType(), map.get("hosGradeCode").toString());
        if (!StringHandler.isNotBlank(gradeName)) {
            res.put("msg","未找到编码对应的医院等级");
            return res;
        }
        hospital.setHosGradeCode(map.get("hosGradeCode").toString().trim());
        hospital.setHosGradeName(gradeName);
        String levelName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_LEVEL.getType(), map.get("hosLevelCode").toString());
        if (!StringHandler.isNotBlank(levelName)) {
            res.put("msg","未找到编码对应的医院级别");
            return res;
        }
        hospital.setHosLevelCode(map.get("hosLevelCode").toString().trim());
        hospital.setHosLevelName(levelName);
        String provinceName = this.areaService.getAreaByCode(map.get("provinceCode").toString());
        if (!StringHandler.isNotBlank(provinceName)) {
            res.put("msg","未找到编码对应的省");
            return res;
        }
        hospital.setProvinceCode(map.get("provinceCode").toString().trim());
        hospital.setProvince(provinceName);
        String cityName = this.areaService.getAreaByCode(map.get("cityCode").toString());
        if (!StringHandler.isNotBlank(cityName)) {
            res.put("msg","未找到编码对应的市");
            return res;
        }
        hospital.setCityCode(map.get("cityCode").toString().trim());
        hospital.setCity(cityName);
        String districtName = this.areaService.getAreaByCode(map.get("districtCode").toString());
        if (!StringHandler.isNotBlank(districtName)) {
            res.put("msg","未找到编码对应的市");
            return res;
        }
        hospital.setDistrictCode(map.get("districtCode").toString().trim());
        hospital.setDistrict(districtName);
        hospital.setAddress(map.get("address").toString().trim());
        hospital.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        //选填参数
        if (StringHandler.isNotBlank(String.valueOf(map.get("hosMobile")))) {
            hospital.setHosMobile(map.get("hosMobile").toString().trim());
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("hosCateCode")))){
            String cateName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_CATE.getType(), map.get("hosCateCode").toString());
            if (!StringHandler.isNotBlank(cateName)) {
                res.put("msg","未找到编码对应的医院类型");
                return res;
            }
            hospital.setHosCateCode(map.get("hosCateCode").toString().trim());
            hospital.setHosCateName(cateName);
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("hosAttCode")))) {
            String attName = this.diceService.getDoctorNameByTypeAndCode(DictTypeEnum.HOS_ATT.getType(), map.get("hosAttCode").toString());
            if (!StringHandler.isNotBlank(attName)) {
                res.put("msg","未找到编码对应的医院属性");
                return res;
            }
            hospital.setHosAttCode(map.get("hosAttCode").toString().trim());
            hospital.setHosAttName(attName);
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("content")))) {
            hospital.setContent(map.get("content").toString().trim());
        }
        if (StringHandler.isNotBlank(String.valueOf(map.get("imgs")))) {
            hospital.setImgs(map.get("imgs").toString().trim());
        }
        this.hospitalMapper.insert(hospital);
        String hospitalNo = IdUtils.getHospitalID(Integer.parseInt(hospital.getId()));
        Hospital hos = new Hospital();
        hos.setId(hospital.getId());
        hos.setHospitalNo(hospitalNo);
        hospitalMapper.updateById(hos);
        //新增科室关联关系
        HospitalDepartment department = new HospitalDepartment();
        department.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        department.setHospitalId(hospital.getId());
        list.forEach(code -> {
            Department one = departmentService.getOne(new QueryWrapper<Department>().eq("code", code));
            department.setDepartCode(code);
            department.setDepartName(one.getName());
            this.hospitalDepartmentMapper.insert(department);
        });
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit(){
                loginRecordUtil.saveLoginRecord(request,"新增医院");
            }
        });
        return res;
    }

    public List<String> filterList(List<String> listA, List<String> listB) {
        List hs1 = new ArrayList(listA);
        List hs2 = new ArrayList(listB);
        hs1.removeAll(hs2);
        List<String> listC = new ArrayList<>();
        listC.addAll(hs1);
        return listC;
    }


    @Override
    public JSONArray provincesRelation() {
        List<Area> areas = this.areaMapper.selectList(new QueryWrapper<>());
        Map<String, List<Area>> areasMap = areas.stream().collect(Collectors.groupingBy(Area::getType));
        List<Area> firstLevel = null;
        List<Area> secondLevel = null;
        List<Area> threeLevel = null;
        for (Map.Entry<String, List<Area>> entry : areasMap.entrySet()) {
            if ("PROVINCE".equals(entry.getKey())) {
                firstLevel = entry.getValue();
            }
            if ("CITY".equals(entry.getKey())) {
                secondLevel = entry.getValue();
            }
            if ("DISTRICT".equals(entry.getKey())) {
                threeLevel = entry.getValue();
            }
        }
        assert firstLevel != null;
        assert secondLevel != null;
        assert threeLevel != null;
        Map<String, List<Area>> secondLevelMap = secondLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        Map<String, List<Area>> threeLevelMap = threeLevel.stream().collect(Collectors.groupingBy(Area::getParentAreaCode));
        JSONArray respArray = new JSONArray();
        firstLevel.forEach(area -> {
            JSONObject resJson = new JSONObject();
            resJson.put("name", area.getName());
            resJson.put("areaCode", area.getAreaCode());
            List<Area> firstSecondRelation = secondLevelMap.get(area.getAreaCode());
            JSONArray secondJsonArray = new JSONArray();
            if (!CollectionUtils.isEmpty(firstSecondRelation)) {
                firstSecondRelation.forEach(s -> {
                    JSONObject jsonSecond = new JSONObject();
                    jsonSecond.put("name", s.getName());
                    jsonSecond.put("areaCode", s.getAreaCode());
                    List<Area> secondThreeRelation = threeLevelMap.get(s.getAreaCode());
                    JSONArray threeJsonArray = new JSONArray();
                    if (!CollectionUtils.isEmpty(secondThreeRelation)) {
                        secondThreeRelation.forEach(area1 -> {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", area1.getName());
                            jsonObject.put("areaCode", area1.getAreaCode());
                            threeJsonArray.add(jsonObject);
                        });
                    }
                    jsonSecond.put("three", threeJsonArray);
                    secondJsonArray.add(jsonSecond);
                });
            }
            resJson.put("second", secondJsonArray);
            respArray.add(resJson);
        });
        return respArray;
    }

    @Override
    public Boolean enableDisable(Map<String, Object> map,HttpServletRequest request) {
        Hospital hospital = new Hospital();
        hospital.setId(map.get("id").toString());
        hospital.setStatus(map.get("status").toString());
        int i = this.hospitalMapper.updateById(hospital);
        loginRecordUtil.saveLoginRecord(request,"更新医院状态");

        return i > 0;
    }

    @Override
    public Boolean updateDel(Map<String, Object> map,HttpServletRequest request) {
        Hospital hospital = new Hospital();
        hospital.setId(map.get("id").toString());
        hospital.setIsDelete(1);
        int i = this.hospitalMapper.updateById(hospital);
        loginRecordUtil.saveLoginRecord(request,"删除医院");
        return i > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONArray hospitalBulkImport(List<HospitalEntity> hospitals,HttpServletRequest request) {
        //验证数据准备
//        // 科室code 验证
        List<Department> departments = departmentMapper.selectList(new QueryWrapper<Department>().eq("status", 1));
        List<String> departmentNames = departments.stream().map(Department::getName).collect(Collectors.toList());
        Map<String, String> departmentMap = departments.stream().collect(Collectors.toMap(Department::getName, Department::getCode));
        //本地提交是否有
        // 错标记
        boolean flag = false;
        JSONArray res = new JSONArray();
        List<Hospital> hospitalList = hospitalMapper.selectList(new QueryWrapper<>());
        List<String> hosNames = hospitalList.stream().filter(hospital -> hospital.getIsDelete() != 1).map(Hospital::getHosName).collect(Collectors.toList());
        for (int i = 0; i < hospitals.size(); i++) {
            HospitalEntity entity = hospitals.get(i);
            // 传入了该值才进行判断，如果没有该值后期自己操作更新对应值
            if (!StringHandler.isNotBlank(entity.getHosName())) {
                res.add("医院名称：" + "第 " + (i + 1) + " 行为空");
                flag = true;
            }
            //医院名重复验证
            if (hosNames.contains(entity.getHosName())){
                res.add("医院名称：" + "第 " + (i + 1) + " 行已存在");
                flag = true;
            }
            if (StringHandler.isNotBlank(entity.getHosGradeName())){
                String gradeCode = this.diceService.getDoctorNameByTypeAndName(DictTypeEnum.HOS_GRADE.getType(), entity.getHosGradeName());
                if (StringHandler.isNotBlank(gradeCode)) {
                    entity.setHosGradeCode(gradeCode);
                } else {
                    res.add("医院等级：" + "第 " + (i + 1) + " 行不存在");
                    flag = true;
                }
            } else {
                res.add("医院等级：" + "第 " + (i + 1) + " 行不存在");
                flag = true;
            }
            if (StringHandler.isNotBlank(entity.getHosLevelName())){
                String levelCode = this.diceService.getDoctorNameByTypeAndName(DictTypeEnum.HOS_LEVEL.getType(), entity.getHosLevelName());
                if (StringHandler.isNotBlank(levelCode)) {
                    entity.setHosLevelCode(levelCode);
                } else {
                    res.add("医院级别：" + "第 " + (i + 1) + " 行不存在");
                    flag = true;
                }
            }

            if (StringHandler.isNotBlank(entity.getHosAttName())){
                String attCode = this.diceService.getDoctorNameByTypeAndName(DictTypeEnum.HOS_ATT.getType(), entity.getHosAttName());
                if (StringHandler.isNotBlank(attCode)) {
                    entity.setHosAttCode(attCode);
                }
            }

            if (StringHandler.isNotBlank(entity.getHosCateName())){
                String cateCode = this.diceService.getDoctorNameByTypeAndName(DictTypeEnum.HOS_CATE.getType(), entity.getHosCateName());
                if (StringHandler.isNotBlank(cateCode)) {
                    entity.setHosCateCode(cateCode);
                }
            }
            if (StringHandler.isNotBlank(entity.getProvince())){
                QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("type","PROVINCE");
                queryWrapper.eq("name",entity.getProvince());
                Area area = areaMapper.selectOne(queryWrapper);
                if (null != area) {
                    entity.setProvinceCode(area.getAreaCode());
                }
            }
            if (StringHandler.isNotBlank(entity.getCity())){
                QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("type","CITY");
                queryWrapper.eq("name",entity.getCity());
                queryWrapper.eq("parent_area_code",entity.getProvinceCode());
                Area area = areaMapper.selectOne(queryWrapper);
                if (null != area) {
                    entity.setCityCode(area.getAreaCode());
                }
            }

            if (StringHandler.isNotBlank(entity.getDistrict())){
                QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("type","DISTRICT");
                queryWrapper.eq("name",entity.getDistrict());
                queryWrapper.eq("parent_area_code",entity.getCityCode());
                Area area = areaMapper.selectOne(queryWrapper);
                if (null != area) {
                    entity.setDistrictCode(area.getAreaCode());
                }
            }

            if (StringHandler.isNotBlank(entity.getDepartName())){
                String[] departNames = entity.getDepartName().split(",");
                List<String> list = new ArrayList<>();
                for (String name : departNames) {
                    if (departmentNames.contains(name)) {
                        list.add(name);
                    }else {
                        res.add("对应的科室：" + "第 " + (i + 1) + " 行科室"+ name +"不存在");
                        flag = true;
                    }
                }
            }else {
                res.add("对应的科室：" + "第 " + (i + 1) + " 行name为空");
                flag = true;
            }

        }
        if (flag) {
            //有错误返回错误行数提示
            return res;
        } else {
            //没有错误，直接保存至数据库
            List<HospitalDepartment> hd = new ArrayList<>();
            Integer userId = Integer.parseInt(SecurityUtils.getLoginUserId());
            hospitals.forEach(hs -> {
                Hospital hospital = new Hospital();
                BeanUtils.copyProperties(hs, hospital);
                hospital.setCreateUserId(String.valueOf(userId));
                String time = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                hospital.setCreateTime(time);
                hospital.setUpdateTime(time);
                this.hospitalMapper.insert(hospital);
                String hospitalNo = IdUtils.getHospitalID(Integer.parseInt(hospital.getId()));
                Hospital hos = new Hospital();
                hos.setId(hospital.getId());
                hos.setHospitalNo(hospitalNo);
                hospitalMapper.updateById(hos);
                if (StringHandler.isNotBlank(hs.getDepartName())){
                    List<String> departNames = Arrays.asList(hs.getDepartName().split(","));
                    departNames.forEach(name -> {
                        HospitalDepartment department = new HospitalDepartment();
                        department.setHospitalId(hospital.getId());
                        department.setCreateUserId(String.valueOf(userId));
                        department.setDepartName(name);
                        department.setDepartCode(departmentMap.get(name));
                        hd.add(department);
                    });
                }
            });
            if (!CollectionUtils.isEmpty(hd)){
                this.hospitalDepartmentService.saveBatch(hd);
            }
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit(){
                    loginRecordUtil.saveLoginRecord(request,"医院批量导入");
                }
            });
            return res;
        }

    }



    @Override
    public void hospitalBulkExport(Map<String,Object> res, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<Hospital> wrapper = new QueryWrapper<>();
        if (null != res.get("hosName") && !"".equals(res.get("hosName").toString())) {
            wrapper.like("hos_name", res.get("hosName"));
        }
        if (null != res.get("hosGradeCode") && !"".equals(res.get("hosGradeCode").toString())) {
            wrapper.eq("hos_grade_code", res.get("hosGradeCode"));
        }
        if (null != res.get("hosCateCode") && !"".equals(res.get("hosCateCode").toString())) {
            wrapper.eq("hos_cate_code", res.get("hosCateCode"));
        }
        if (null != res.get("hosLevelCode") && !"".equals(res.get("hosLevelCode").toString())) {
            wrapper.eq("hos_level_code", res.get("hosLevelCode"));
        }
        if (null != res.get("hosAttCode") && !"".equals(res.get("hosAttCode").toString())) {
            wrapper.eq("hos_att_code", res.get("hosAttCode"));
        }
        List<Hospital> hospitals = this.hospitalMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(hospitals)) {
            throw new BizException("当前无医院数据");
        }
        List<String> ids = hospitals.stream().map(Hospital::getId).collect(Collectors.toList());
        List<HospitalDepartment> hds = this.hospitalDepartmentService.list(new QueryWrapper<HospitalDepartment>().in("hospital_id", ids));
        if (CollectionUtils.isEmpty(hds)) {
            throw new BizException("根据医院查询科室为空，id为："+ids);
        }
        Map<String, List<HospitalDepartment>> map = hds.stream().collect(Collectors.groupingBy(HospitalDepartment::getHospitalId));
        List<HospitalVo> list = new ArrayList<>();
        //查询医生数量
        List<DoctorUser> doctorUsers = doctorUserMapper.findByDoctorUserIds(ids);
        Map<String, List<DoctorUser>> listMap = doctorUsers.stream().filter(v -> v.getIsCancel() != "1")
                .collect(Collectors.groupingBy(DoctorUser::getHospitalId));
        hospitals.forEach(hospital -> {
            HospitalVo vo = new HospitalVo();
            BeanUtils.copyProperties(hospital, vo);
            List<HospitalDepartment> hospitalDepartments = map.get(hospital.getId());
            if (hospitalDepartments != null) {
                Map<String, String> depart = hospitalDepartments.stream().collect(Collectors.toMap(HospitalDepartment::getDepartCode, HospitalDepartment::getDepartName));
                vo.setDepartName(JSONUtil.toJson(depart));
            }
            if (!StringUtils.isEmpty(listMap.get(hospital.getId()))) {
                long count = listMap.get(hospital.getId()).stream().count();
                vo.setDoctorCount(String.valueOf(count));
            }
            list.add(vo);
        });
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "医院表格.xls";
        ExcelUtils.writeExcel(request, response, list, HospitalVo.class, fileName);
    }


    @Override
    public void hospitalDepartBulkExport(HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<Hospital> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1);
        wrapper.eq("is_delete",0);
        List<Hospital> hospitals = this.hospitalMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(hospitals)) {
            throw new BizException("当前无医院数据");
        }
        List<HospitalListVo> listVos = hospitals.stream().map(hospital -> {
            HospitalListVo vo = new HospitalListVo();
            vo.setId(hospital.getId());
            vo.setHosName(hospital.getHosName());
            List<HospitalDepartment> departments = hospitalDepartmentMapper.selectList(new QueryWrapper<HospitalDepartment>()
                    .eq("hospital_id", hospital.getId()));
            List<String> departs = departments.stream().map(department -> {
                String depart = department.getDepartCode() + ":" + department.getDepartName();
                return depart;
            }).collect(Collectors.toList());
            vo.setDepartList(departs);
            return vo;
        }).collect(Collectors.toList());
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "医院科室表格.xls";
        ExcelUtils.writeExcel(request, response, listVos, HospitalListVo.class, fileName);
    }
}
