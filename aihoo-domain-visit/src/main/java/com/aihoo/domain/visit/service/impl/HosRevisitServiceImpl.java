package com.aihoo.domain.visit.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.visit.model.entity.HosRevisit;
import com.aihoo.domain.visit.model.mapper.HosRevisitMapper;
import com.aihoo.domain.visit.model.vo.RevisitOrderTradeInfoVo;
import com.aihoo.domain.visit.service.HosRevisitService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.util.StatusEnumUtil;
import com.aihoo.util.StringHandler;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 复诊信息表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-11-02
 */
@Service
public class HosRevisitServiceImpl extends ServiceImpl<HosRevisitMapper, HosRevisit> implements HosRevisitService {

    @Autowired
    private HosRevisitMapper hosRevisitMapper;

    public PageResult<RevisitOrderTradeInfoVo> page(Map<String, Object> param) throws Exception{
        Map<String, Object> map = new HashMap<>();
        int page = 1;
        int limit = 10;
        if (StringHandler.isNotBlank(String.valueOf(param.get("Range")))){
            if ("0".equals(param.get("Range"))){//当月
                //本月第一天
                String startTime = DateUtil.getUpMonthLastDay(DateUtil.DATE_FORMAT_1);
                //当前时间
                String endTime = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                map.put("startTime",startTime);
                map.put("endTime",endTime);
            }else if ("1".equals(param.get("Range"))){//上月
                //上月第一天
                String startTime = DateUtil.getUpMonthFirstDay(DateUtil.DATE_FORMAT_1);
                //上月最后一天
                String endTime = DateUtil.getUpMonthLastDay(DateUtil.DATE_FORMAT_1);
                map.put("startTime",startTime);
                map.put("endTime",endTime);
            }else if ("2".equals(param.get("Range"))){//半年
                //当前时间
                String endTime = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                //半年前
                String startTime = DateUtil.addOrSubTime(endTime,1,-6,DateUtil.DATE_FORMAT_1);
                map.put("startTime",startTime);
                map.put("endTime",endTime);
            }else if ("3".equals(param.get("Range"))){
                //当前时间
                String endTime = DateUtil.getTime(DateUtil.DATE_FORMAT_1);
                //1年前
                String startTime = DateUtil.addOrSubTime(endTime,0,-1,DateUtil.DATE_FORMAT_1);
                map.put("startTime",startTime);
                map.put("endTime",endTime);
            }
        }
        //查询参数
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
        }else {
            map.put("status", "\'DONE\',\'PAY\'");
        }
        //分页参数
        if (StringHandler.isNotBlank(String.valueOf(param.get("page")))) {
            page = Integer.parseInt(param.get("page").toString());
        }
        if (StringHandler.isNotBlank(String.valueOf(param.get("limit")))) {
            limit = Integer.parseInt(param.get("limit").toString());
        }
        map.put("page", (page-1)*limit);
        map.put("limit", limit);
        List<RevisitOrderTradeInfoVo> list = hosRevisitMapper.getInfos(map);
        List<RevisitOrderTradeInfoVo> infoVos = list.stream().map(v -> {
            if (StringHandler.isNotBlank(v.getPayType())) {
                String payType = StatusEnumUtil.getPayType(v.getPayType());
                v.setPayType(payType);
            }
            String revisitStatus = StatusEnumUtil.getRevisitStatus(v.getStatus());
            v.setStatus(revisitStatus);
            return v;
        }).collect(Collectors.toList());
        int total = hosRevisitMapper.getTotal(map);
        return new PageResult<>(infoVos, total);
    }


    @Override
    public void revisitOrderOutExecl(Map<String,Object> param, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        //查询参数
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
        }else {
            map.put("status", "\'DONE\',\'PAY\'");
        }
        List<RevisitOrderTradeInfoVo> list = hosRevisitMapper.getInfos(map);
        if (CollectionUtils.isEmpty(list)) {
            throw new BizException("当前无复诊订单交易记录数据");
        }
        List<RevisitOrderTradeInfoVo> infoVos = list.stream().map(v -> {
            if (StringHandler.isNotBlank(v.getPayType())) {
                String payType = StatusEnumUtil.getPayType(v.getPayType());
                v.setPayType(payType);
            }
            String revisitStatus = StatusEnumUtil.getRevisitStatus(v.getStatus());
            v.setStatus(revisitStatus);
            return v;
        }).collect(Collectors.toList());
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "复诊订单交易记录表格.xls";
        ExcelUtils.writeExcel(request, response, infoVos, RevisitOrderTradeInfoVo.class, fileName);
    }
}
