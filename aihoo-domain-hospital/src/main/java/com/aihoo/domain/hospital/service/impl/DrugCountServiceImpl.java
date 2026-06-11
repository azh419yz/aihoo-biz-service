package com.aihoo.domain.hospital.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.hospital.model.entity.DrugCount;
import com.aihoo.domain.hospital.model.mapper.DrugCountMapper;
import com.aihoo.domain.hospital.service.DrugCountService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.util.StatusEnumUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 药品对账表 服务实现
 *
 * <p>从旧 admin 的 com.aihoo.admin.system.service.impl.DrugCountServiceImpl 迁入。
 * 包路径统一为 com.aihoo.domain.hospital.* + com.aihoo.util.* + com.aihoo.excel.* +
 * com.aihoo.exception.* + com.aihoo.common.*。</p>
 *
 * @author lx
 * @since 2020-11-04
 */
@Service
public class DrugCountServiceImpl extends ServiceImpl<DrugCountMapper, DrugCount> implements DrugCountService {

    @Override
    public PageResult<DrugCount> page(Map<String, Object> map) {
        int page = 1;
        int limit = 10;
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Integer.parseInt(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Integer.parseInt(map.get("limit").toString());
        }
        int fromIndex = limit * (page - 1);
        map.put("page", fromIndex);
        map.put("limit", limit);
        List<DrugCount> drugCounts = baseMapper.selectDrugs(map);
        Integer count = baseMapper.selectDrugsCount(map);
        return new PageResult<>(drugCounts, count);
    }

    @Override
    public void drugCountOutExecl(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        List<DrugCount> drugCounts = this.baseMapper.selectDrugsExecl(map);
        List<DrugCount> infoVos = drugCounts.stream().peek(v -> {
            String payType = StatusEnumUtil.getPayType(v.getPayType());
            v.setPayType(payType);
            v.setTradeStatus(v.getTradeStatus().equals("0") ? "成功" : "失败");
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoVos)) {
            throw new BizException("当前无药品对账数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "药品对账表格.xls";
        ExcelUtils.writeExcel(request, response, infoVos, DrugCount.class, fileName);
    }
}
