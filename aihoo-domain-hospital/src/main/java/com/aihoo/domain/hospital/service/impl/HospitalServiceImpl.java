package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.Hospital;
import com.aihoo.domain.hospital.model.excel.HospitalEntity;
import com.aihoo.domain.hospital.model.mapper.HospitalMapper;
import com.aihoo.domain.hospital.model.vo.HospitalPageVo;
import com.aihoo.domain.hospital.service.HospitalService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Classname HospitalServiceImpl
 * @Description 医院服务实现
 *
 * <p>当前状态：占位实现，方法签名已与旧代码对齐；具体业务逻辑待完整迁移。
 * 涉及 B 类工具类（ExcelUtils/DateUtil/IdUtils/LoginRecordUtil/StringHandler）
 * 尚未迁入 shared-kernel，因此暂不实现。</p>
 *
 * @Date 2020/9/16 20:43
 * @Created by ad
 */
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    @Override
    public PageResult<HospitalPageVo> list(Map<String, Object> map) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.list 完整实现");
    }

    @Override
    public JSONObject controlHospital(String id) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.controlHospital 完整实现");
    }

    @Override
    public JSONArray departmentRelation() {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.departmentRelation 完整实现");
    }

    @Override
    public JSONObject hospitalUpdate(Map<String, Object> map, HttpServletRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.hospitalUpdate 完整实现");
    }

    @Override
    public JSONObject hospitalInsert(Map<String, Object> map, HttpServletRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.hospitalInsert 完整实现");
    }

    @Override
    public JSONArray provincesRelation() {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.provincesRelation 完整实现");
    }

    @Override
    public Boolean enableDisable(Map<String, Object> map, HttpServletRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.enableDisable 完整实现");
    }

    @Override
    public Boolean updateDel(Map<String, Object> map, HttpServletRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.updateDel 完整实现");
    }

    @Override
    public JSONArray hospitalBulkImport(List<HospitalEntity> hospitals, HttpServletRequest request) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.hospitalBulkImport 完整实现");
    }

    @Override
    public void hospitalBulkExport(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.hospitalBulkExport 完整实现");
    }

    @Override
    public void hospitalDepartBulkExport(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: 迁移 HospitalServiceImpl.hospitalDepartBulkExport 完整实现");
    }
}
