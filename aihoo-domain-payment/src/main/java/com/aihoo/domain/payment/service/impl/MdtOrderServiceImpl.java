package com.aihoo.domain.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aihoo.common.PageParam;
import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.service.DrugstoreService;
import com.aihoo.domain.payment.model.entity.MdtOrder;
import com.aihoo.domain.payment.model.request.SearchMdtOrderRequest;
import com.aihoo.domain.payment.model.vo.MdtOrderTradeInfoVo;
import com.aihoo.domain.payment.model.vo.MdtOrderVo;
import com.aihoo.domain.payment.model.excel.MdtOrderDrugExportEntity;
import com.aihoo.domain.payment.model.excel.MdtOrderExportEntity;
import com.aihoo.domain.payment.model.vo.PrescriptionDrugDTO;
import com.aihoo.domain.payment.service.MdtOrderService;
import com.aihoo.exception.BizException;
import com.aihoo.util.DateUtil;
import com.aihoo.util.HttpUtil;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.StringHandler;
import com.aihoo.util.StringUtil;
import com.aihoo.excel.ExcelColumn;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会诊订单 service 实现
 *
 * <p>依赖 prescription 域：HosPrescriptionService / HosPreDrugService /
 * PrescriptionInstructionService；依赖 hospital 域：DrugstoreService。
 * 等相关域迁入后再注入。</p>
 *
 * @author Lenovo
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MdtOrderServiceImpl extends ServiceImpl<com.aihoo.domain.payment.model.mapper.MdtOrderMapper, MdtOrder> implements MdtOrderService {

    private final com.aihoo.domain.payment.model.mapper.MdtOrderMapper mdtOrderMapper;

    // 跨域依赖 stub：prescription 域注入会与 prescription → payment 形成循环依赖，保持 stub
    // private final HosPrescriptionService prescriptionService;
    // private final HosPreDrugService predrugService;
    // private final PrescriptionInstructionService prescriptionInstructionService;
    // hospital 域已在 pom 中依赖，安全注入
    private final DrugstoreService drugstoreService;

    @Value("${pst.endpoint:pst.heouai.com}")
    private String pstEndpoint;

    @Value("${pst.api.generate-pdf:/api/print/async}")
    private String pstGeneratePdfApi;

    @Value("${pst.parameter.generate-pdf-url:https://manage.heouai.com/chufang?id=}")
    private String pstGeneratePdfUrl;


    @Override
    public PageResult<MdtOrderVo> getPage(PageParam<MdtOrder> pageParam, SearchMdtOrderRequest request) {

        LambdaQueryWrapper<MdtOrder> queryWrapper = getMdtOrderLambdaQueryWrapper(request);

        // 执行分页查询
        Page<MdtOrder> page = baseMapper.selectPage(pageParam, queryWrapper);

        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new PageResult<>();
        }

        List<MdtOrderVo> voList = new ArrayList<>();
        List<String> preIdList = page.getRecords().stream().map(MdtOrder::getPreId).toList();

        // TODO: 等 prescription 域迁入后恢复
        // Map<String, HosPrescription> prescriptionMap = prescriptionService.listByIds(preIdList)
        //         .stream().collect(Collectors.toMap(HosPrescription::getId, s -> s));
        Map<String, Object> prescriptionMap = new HashMap<>();


        for (MdtOrder mdtOrder : page.getRecords()) {
            MdtOrderVo vo = new MdtOrderVo();
            BeanUtils.copyProperties(mdtOrder, vo);
            if (StringHandler.isNotBlank(mdtOrder.getPic())) {
                vo.setPicList(Arrays.asList(mdtOrder.getPic().split(",")));
            }

            // TODO: 等 prescription 域迁入后填充药品和药房信息
            // HosPrescription prescription = prescriptionMap.get(mdtOrder.getPreId());
            // List<HosPrescriptionDrug> drugList = predrugService.list(new LambdaQueryWrapper<HosPrescriptionDrug>()
            //         .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));
            // List<PrescriptionDrugDTO> drugDTOList = drugList.stream().map(prescriptionDrug -> {
            //     PrescriptionDrugDTO dto = new PrescriptionDrugDTO();
            //     BeanUtil.copyProperties(prescriptionDrug, dto);
            //     return dto;
            // }).toList();
            // vo.setDrugList(drugDTOList);
            //
            // HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
            //         .eq(HosPrescriptionInstruction::getHosPrescriptionId, prescription.getId()));
            // vo.setDoseNumber(prescriptionInstruction != null ? prescriptionInstruction.getDoseNumber() : null);
            //
            // Drugstore drugstore = drugstoreService.getById(mdtOrder.getDrugstoreId());
            // vo.setDrugstoreName(drugstore != null ? drugstore.getName() : null);

            voList.add(vo);
        }
        return new PageResult<>(voList, page.getTotal());
    }

    private static LambdaQueryWrapper<MdtOrder> getMdtOrderLambdaQueryWrapper(SearchMdtOrderRequest request) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = new LambdaQueryWrapper<MdtOrder>()
                .in(MdtOrder::getStatus, request.getStatusList())
                .like(StringUtil.isNotBlank(request.getHosSickName()), MdtOrder::getName, request.getHosSickName())
                .like(StringUtil.isNotBlank(request.getReceiveName()), MdtOrder::getReceiveName, request.getReceiveName())
                .eq(StringUtil.isNotBlank(request.getReceivePhone()), MdtOrder::getReceivePhone, request.getReceivePhone())
                .eq(StringUtil.isNotBlank(request.getOrderId()), MdtOrder::getId, request.getOrderId())
                .eq(StringUtil.isNotBlank(request.getPreId()), MdtOrder::getPreId, request.getPreId())
                .eq(StringUtil.isNotBlank(request.getDrugstoreId()), MdtOrder::getDrugstoreId, request.getDrugstoreId())
                .eq(StringUtil.isNotBlank(request.getStatus()), MdtOrder::getStatus, request.getStatus())
                .eq(request.getMedicineStatus() != null, MdtOrder::getMedicineStatusCode, request.getMedicineStatus())
                .ge(StringUtil.isNotBlank(request.getPayStartTime()), MdtOrder::getPayTime, request.getPayStartTime())
                .le(StringUtil.isNotBlank(request.getPayEndTime()), MdtOrder::getPayTime, request.getPayEndTime())
                .orderByDesc(MdtOrder::getPayTime);
        if (request.getHavePic() != null) {
            if (request.getHavePic()) {
                queryWrapper.isNotNull(MdtOrder::getPic);
            } else {
                queryWrapper.isNull(MdtOrder::getPic);
            }
        }
        return queryWrapper;
    }

    @Override
    public Long count(List<String> statusList) {
        return count(new LambdaQueryWrapper<MdtOrder>().in(MdtOrder::getStatus, statusList));
    }

    @Override
    public PageResult<MdtOrderTradeInfoVo> page(Map<String, Object> param) throws Exception {
        Map<String, Object> map = new HashMap<>();
        int page = 1;
        int limit = 10;
        if (StringHandler.isNotBlank(String.valueOf(param.get("Range")))) {
            if ("0".equals(param.get("Range"))) {//当月
                String startTime = DateUtil.getUpMonthLastDay(DateUtil.DATE_FORMAT_1);
                String endTime = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
            } else if ("1".equals(param.get("Range"))) {//上月
                String startTime = DateUtil.getUpMonthFirstDay(DateUtil.DATE_FORMAT_1);
                String endTime = DateUtil.getUpMonthLastDay(DateUtil.DATE_FORMAT_1);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
            } else if ("2".equals(param.get("Range"))) {//半年
                String endTime = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                String startTime = DateUtil.addOrSubTime(endTime, 1, -6, DateUtil.DATE_FORMAT_1);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
            } else if ("3".equals(param.get("Range"))) {
                String endTime = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                String startTime = DateUtil.addOrSubTime(endTime, 0, -1, DateUtil.DATE_FORMAT_1);
                map.put("startTime", startTime);
                map.put("endTime", endTime);
            }
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("startTime")))) {
            map.put("startTime", param.get("startTime"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("endTime")))) {
            map.put("endTime", param.get("endTime"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("hosSickName")))) {
            map.put("hosSickName", param.get("hosSickName"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("doctorName")))) {
            map.put("doctorName", param.get("doctorName"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("payType")))) {
            map.put("payType", param.get("payType"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("hospitalName")))) {
            map.put("hospitalName", param.get("hospitalName"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("status")))) {
            map.put("status", "\'" + param.get("status") + "\'");
        } else {
            map.put("status", "\'PAY\',\'DONE\'");
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("page")))) {
            page = Integer.parseInt(param.get("page").toString());
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("limit")))) {
            limit = Integer.parseInt(param.get("limit").toString());
        }
        map.put("page", (page - 1) * limit);
        map.put("limit", limit);
        List<MdtOrderTradeInfoVo> list = mdtOrderMapper.getInfos(map);
        List<MdtOrderTradeInfoVo> infoVos = list.stream().peek(v -> {
            if (StringHandler.isNotBlank(v.getPayType())) {
                String payType = StatusEnumUtil.getPayType(v.getPayType());
                v.setPayType(payType);
            }
            String mdtOrderStatus = StatusEnumUtil.getMdtOrderStatus(v.getStatus());
            v.setStatus(mdtOrderStatus);
        }).collect(Collectors.toList());
        int total = mdtOrderMapper.getTotal(map);
        return new PageResult<>(infoVos, total);
    }


    @Override
    public void mdtOrderOutExecl(Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (StringHandler.isNotBlank(String.valueOf(param.get("startTime")))) {
            map.put("startTime", param.get("startTime"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("endTime")))) {
            map.put("endTime", param.get("endTime"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("hosSickName")))) {
            map.put("hosSickName", param.get("hosSickName"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("doctorName")))) {
            map.put("doctorName", param.get("doctorName"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("payType")))) {
            map.put("payType", param.get("payType"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("hospitalName")))) {
            map.put("hospitalName", param.get("hospitalName"));
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("status")))) {
            map.put("status", "'" + param.get("status") + "'");
        } else {
            map.put("status", "'PAY','DONE'");
        }
        List<MdtOrderTradeInfoVo> list = mdtOrderMapper.getInfos(map);
        if (CollectionUtils.isEmpty(list)) {
            throw new BizException("当前无会诊订单交易记录数据");
        }
        List<MdtOrderTradeInfoVo> infoVos = list.stream().peek(v -> {
            if (StringHandler.isNotBlank(v.getPayType())) {
                String payType = StatusEnumUtil.getPayType(v.getPayType());
                v.setPayType(payType);
            }
            String mdtOrderStatus = StatusEnumUtil.getMdtOrderStatus(v.getStatus());
            v.setStatus(mdtOrderStatus);
        }).collect(Collectors.toList());
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "会诊订单交易记录表格.xls";
        com.aihoo.excel.ExcelUtils.writeExcel(request, response, infoVos, MdtOrderTradeInfoVo.class, fileName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String printPdf(String orderId) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (!"UNALLOCATED".equals(order.getStatus()) && !"ALLOCATING".equals(order.getStatus())) {
            throw new BizException("当前订单已完成调配，无需重复调配");
        }
        if ("UNALLOCATED".equals(order.getStatus())) {
            order.setStatus("ALLOCATING");
        }
        order.setPdfFlag("1");
        updateById(order);

        String prescriptionId = order.getPreId();
        if (prescriptionId == null || prescriptionId.isEmpty()) {
            throw new BizException("处方ID不存在");
        }

        String pdfUrl = pstGeneratePdfUrl + prescriptionId + "&type=pdf";
        String apiUrl = "https://" + pstEndpoint + pstGeneratePdfApi + "?url=" + pdfUrl;

        log.info("调用PST接口生成PDF, url: {}", apiUrl);
        String response = HttpUtil.getForObject(apiUrl, String.class);
        log.info("PST接口响应: {}", response);

        if (response == null || response.isEmpty()) {
            throw new BizException("PDF生成失败，PST接口无响应");
        }

        try {
            int code = JSONUtil.getIntValue(response, "code");
            if (code != 200) {
                String msg = JSONUtil.getString(response, "msg");
                throw new BizException(msg.isEmpty() ? "PDF生成失败" : msg);
            }
            String dataStr = JSONUtil.getString(response, "data");
            if (dataStr == null || dataStr.isEmpty()) {
                throw new BizException("PDF生成失败，未获取到数据");
            }
            String filePath = JSONUtil.getString(dataStr, "filePath");
            if (filePath == null || filePath.isEmpty()) {
                throw new BizException("PDF生成失败，未获取到文件路径");
            }
            return filePath;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("PDF生成响应解析失败", e);
            throw new BizException("PDF生成失败，响应解析异常");
        }
    }

    @Override
    public boolean printExpress(String orderId) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if ("UNALLOCATED".equals(order.getStatus())) {
            order.setStatus("ALLOCATING");
        }
        order.setExpressFlag("1");
        return updateById(order);
    }

    @Override
    public boolean completeAllocation(String orderId) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if ("PENDING_SHIPMENT".equals(order.getStatus()) || "IN_TRANSIT".equals(order.getStatus()) || "COMPLETED".equals(order.getStatus())) {
            throw new BizException("当前订单已完成调配，无需重复操作。");
        }
        if ("REFUNDING".equals(order.getStatus()) || "REFUNDED".equals(order.getStatus())) {
            throw new BizException("当前订单已经发起退款，无法进行调配工作。");
        }
        order.setStatus("PENDING_SHIPMENT");
        return updateById(order);
    }

    @Override
    public boolean saveRemarkAndPic(String orderId, String remark, List<String> picList) {
        MdtOrder order = getById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (StringHandler.isNotBlank(remark)) {
            order.setRemark(remark);
        }
        if (picList != null && !picList.isEmpty()) {
            order.setPic(String.join(",", picList));
        }
        return updateById(order);
    }

    @Override
    public void export(SearchMdtOrderRequest orderRequest, HttpServletRequest request, HttpServletResponse response) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = getMdtOrderLambdaQueryWrapper(orderRequest);
        List<MdtOrder> orderList = baseMapper.selectList(queryWrapper);
        List<MdtOrderExportEntity> entityList = new ArrayList<>();

        List<String> preIdList = orderList.stream().map(MdtOrder::getPreId).toList();

        // TODO: prescription 域 stub
        Map<String, Object> prescriptionMap = new HashMap<>();

        for (MdtOrder order : orderList) {
            MdtOrderExportEntity exportEntity = new MdtOrderExportEntity();
            BeanUtils.copyProperties(order, exportEntity);
            exportEntity.setOrderId(order.getId());

            // TODO: prescription 域 stub
            // HosPrescription prescription = prescriptionMap.get(order.getPreId());
            // HosPrescriptionInstruction prescriptionInstruction = prescriptionInstructionService.getOne(new LambdaQueryWrapper<HosPrescriptionInstruction>()
            //         .eq(HosPrescriptionInstruction::getHosPrescriptionId, prescription.getId()));
            // exportEntity.setDoseNumber(prescriptionInstruction != null ? prescriptionInstruction.getDoseNumber() : null);
            // exportEntity.setPrice(prescription.getTotalPrice());

            entityList.add(exportEntity);
        }

        com.aihoo.excel.ExcelUtils.writeExcel(request, response, entityList, MdtOrderExportEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "订单数据表格.xlsx");

    }

    @Override
    public void drugExport(SearchMdtOrderRequest orderRequest, HttpServletRequest request, HttpServletResponse response) {
        LambdaQueryWrapper<MdtOrder> queryWrapper = getMdtOrderLambdaQueryWrapper(orderRequest);
        List<MdtOrder> orderList = baseMapper.selectList(queryWrapper);

        List<String> preIdList = orderList.stream().map(MdtOrder::getPreId).toList();

        // TODO: prescription 域 stub
        Map<String, Object> prescriptionMap = new HashMap<>();

        List<MdtOrderDrugExportEntity> mdtOrderDrugExportEntityList = new ArrayList<>();
        for (MdtOrder order : orderList) {
            // TODO: prescription 域 stub
            // HosPrescription prescription = prescriptionMap.get(order.getPreId());
            // List<HosPrescriptionDrug> drugList = predrugService.list(new LambdaQueryWrapper<HosPrescriptionDrug>()
            //         .eq(HosPrescriptionDrug::getHosPrescriptionId, prescription.getId()));
            // mdtOrderDrugExportEntityList.addAll(drugList.stream().map(prescriptionDrug -> {
            //     MdtOrderDrugExportEntity entity = new MdtOrderDrugExportEntity();
            //     BeanUtil.copyProperties(prescriptionDrug, entity);
            //     entity.setUnit("g");
            //     return entity;
            // }).toList());
        }
        com.aihoo.excel.ExcelUtils.writeExcel(request, response, mdtOrderDrugExportEntityList, MdtOrderDrugExportEntity.class, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")) + "订单用量数据表格.xlsx");

    }
}