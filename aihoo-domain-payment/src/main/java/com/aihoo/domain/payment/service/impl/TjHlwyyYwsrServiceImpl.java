package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.TjHlwyyYwsr;
import com.aihoo.domain.payment.model.mapper.TjHlwyyYwsrMapper;
import com.aihoo.domain.payment.service.TjHlwyyYwsrService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 互联网医院业务收入统计表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Service
@Slf4j
public class TjHlwyyYwsrServiceImpl extends ServiceImpl<TjHlwyyYwsrMapper, TjHlwyyYwsr> implements TjHlwyyYwsrService {


    @Autowired
    private TjHlwyyYwsrMapper tjHlwyyYwsrMapper;


    @Override
    public PageResult<TjHlwyyYwsr> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<TjHlwyyYwsr> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.apply("KSBM IN (SELECT d.`WSJDM` FROM TB_DIC_Department d WHERE d.`KSTYBZ` = 1)");
        wrapper.apply("IFNULL(HLWZLZSR,0) + IFNULL(HLWZLYPSR,0) + IFNULL(HLWZLYBSR,0) + IFNULL(HLWZLZFSR,0) > 0");
        wrapper.orderByDesc("YWSJ");
        wrapper.orderByDesc("create_time");
        IPage<TjHlwyyYwsr> iPage = tjHlwyyYwsrMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void tjHlwyyYwsrOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<TjHlwyyYwsr> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.apply("KSBM IN (SELECT d.`WSJDM` FROM TB_DIC_Department d WHERE d.`KSTYBZ` = 1)");
        wrapper.apply("IFNULL(HLWZLZSR,0) + IFNULL(HLWZLYPSR,0) + IFNULL(HLWZLYBSR,0) + IFNULL(HLWZLZFSR,0) > 0");
        wrapper.orderByDesc("YWSJ");
        wrapper.orderByDesc("create_time");
        List<TjHlwyyYwsr> tjHlwyyYwsrs = this.tjHlwyyYwsrMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(tjHlwyyYwsrs)) {
            throw new BizException("当前无业务收入统计表数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "业务收入统计表格.xls";
        ExcelUtils.writeExcel(request, response, tjHlwyyYwsrs, TjHlwyyYwsr.class, fileName);
    }
}