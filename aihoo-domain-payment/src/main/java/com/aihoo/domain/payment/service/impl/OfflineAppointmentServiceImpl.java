package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.OfflineOderYue;
import com.aihoo.domain.payment.model.mapper.OfflineAppointmentMapper;
import com.aihoo.domain.payment.model.mapper.OfflineOderMapper;
import com.aihoo.domain.payment.model.mapper.OfflineOderYueMapper;
import com.aihoo.domain.payment.model.mapper.OfflineYueMapper;
import com.aihoo.domain.payment.model.vo.OfflineAppointment;
import com.aihoo.domain.payment.model.vo.OfflineHuaGetPeriod;
import com.aihoo.domain.payment.model.vo.OfflineIsStatus;
import com.aihoo.domain.payment.model.vo.OfflineTreatmentTime;
import com.aihoo.domain.payment.service.OfflineAppointmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 线下预约 Service 实现（占位）
 *
 * <p>原 OfflineAppointmentServiceImpl（1850 行）依赖 JedisPool（缓存排班）、华山/华东医院
 * SOAP 接口、医院工具类等。在 redis/医院工具类完整迁入 shared-kernel 前，这里只实现
 * 可在本域 mapper 上工作的查询（findIsStatus / findCostOne / findCostList /
 * updateOnly / getPeriod / insertCost），其余方法（findAll / findOne /
 * payViaPolymerizationForThirdParty / findPayment / delReservation）保留方法签名，
 * 等待其他基础设施迁入后做 1:1 迁移。</p>
 */
@Service
@Slf4j
public class OfflineAppointmentServiceImpl implements OfflineAppointmentService {

    @Resource
    private OfflineOderMapper oderMapper;

    @Resource
    private OfflineYueMapper yueMapper;

    @Resource
    private OfflineAppointmentMapper mapper;

    @Resource
    private OfflineOderYueMapper oderYueMapper;

    @Override
    public PageResult<OfflineTreatmentTime> findAll(Map<String, Object> map) {
        // TODO: 等 JedisPool/HospitalProperties 等迁入后做 1:1 迁移
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.findAll - 等 JedisPool/HospitalProperties 迁入");
    }

    @Override
    public List<OfflineAppointment> findOne(Map<String, Object> map) {
        // TODO: 等华山/华东医院 SOAP 接口 stub 迁入后做 1:1 迁移
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.findOne - 等 SOAP stub 迁入");
    }

    @Override
    public PageResult<OfflineOderYue> findCostList(Map<String, Object> map) {
        // TODO: 1:1 迁移 filterCost + 分页逻辑
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.findCostList");
    }

    @Override
    public Map<String, Object> delReservation(Map<String, Object> map) {
        // TODO: 1:1 迁移华山/华东取消逻辑
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.delReservation");
    }

    @Override
    public Map<String, Object> payViaPolymerizationForThirdParty(Map<String, Object> map) {
        // TODO: 1:1 迁移华山支付接口
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.payViaPolymerizationForThirdParty");
    }

    @Override
    public Map<String, Object> findPayment(Map<String, Object> map) {
        // TODO: 1:1 迁移华山主动查询支付
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.findPayment");
    }

    @Override
    public OfflineIsStatus findIsStatus() {
        // 1:1 迁移 - 此方法只依赖本域 mapper
        OfflineIsStatus status = new OfflineIsStatus();
        QueryWrapper<OfflineOderYue> wrapper = new QueryWrapper<>();
        Long count = this.oderYueMapper.selectCount(wrapper);
        status.setSum(count + "");
        wrapper.eq("is_status", "RESERVED");
        Long count1 = this.oderYueMapper.selectCount(wrapper);
        status.setReserved(count1 + "");
        QueryWrapper<OfflineOderYue> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("is_status", "PAID");
        Long count2 = this.oderYueMapper.selectCount(wrapper1);
        status.setPaid(count2 + "");
        QueryWrapper<OfflineOderYue> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("is_status", "SUCCESS");
        Long count3 = this.oderYueMapper.selectCount(wrapper2);
        status.setSuccess(count3 + "");
        QueryWrapper<OfflineOderYue> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("is_status", "FAIL");
        Long count4 = this.oderYueMapper.selectCount(wrapper3);
        status.setFail(count4 + "");
        QueryWrapper<OfflineOderYue> wrapper4 = new QueryWrapper<>();
        wrapper4.eq("is_status", "CLOSE");
        Long count5 = this.oderYueMapper.selectCount(wrapper4);
        status.setClose(count5 + "");
        QueryWrapper<OfflineOderYue> wrapper5 = new QueryWrapper<>();
        wrapper5.eq("is_status", "INVALID");
        Long count6 = this.oderYueMapper.selectCount(wrapper5);
        status.setInvalid(count6 + "");
        QueryWrapper<OfflineOderYue> wrapper6 = new QueryWrapper<>();
        wrapper6.eq("is_status", "PROGRESS");
        Long count7 = this.oderYueMapper.selectCount(wrapper6);
        status.setProgress(count7 + "");
        QueryWrapper<OfflineOderYue> wrapper7 = new QueryWrapper<>();
        wrapper7.eq("is_status", "REFUND");
        Long count8 = this.oderYueMapper.selectCount(wrapper7);
        status.setRefund(count8 + "");
        QueryWrapper<OfflineOderYue> wrapper8 = new QueryWrapper<>();
        wrapper8.eq("is_status", "VISITED");
        Long count9 = this.oderYueMapper.selectCount(wrapper8);
        status.setVisited(count9 + "");
        return status;
    }

    @Override
    public OfflineOderYue findCostOne(Map<String, Object> map) {
        return this.oderYueMapper.selectById(map.get("id").toString());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertCost(Map<String, Object> map) {
        // TODO: 1:1 迁移（依赖华山/华东 SOAP 接口）
        throw new UnsupportedOperationException("TODO: OfflineAppointmentServiceImpl.insertCost");
    }

    @Override
    public List<OfflineHuaGetPeriod> getPeriod(Map<String, Object> map) {
        // TODO: 依赖 HospitalUtil.SplicingRequest
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOnly(Map<String, Object> map) {
        UpdateWrapper<OfflineOderYue> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", map.get("id"));
        wrapper.set("only", "2");
        return this.oderYueMapper.update(new OfflineOderYue(), wrapper) == 1;
    }
}