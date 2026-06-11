package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.TjHlwyyYwl;
import com.aihoo.domain.payment.model.mapper.TjHlwyyYwlMapper;
import com.aihoo.domain.payment.service.TjHlwyyYwlService;
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
 * 互联网医院业务量统计表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
@Service
@Slf4j
public class TjHlwyyYwlServiceImpl extends ServiceImpl<TjHlwyyYwlMapper, TjHlwyyYwl> implements TjHlwyyYwlService {

    @Autowired
    private TjHlwyyYwlMapper tjHlwyyYwlMapper;

    @Override
    public PageResult<TjHlwyyYwl> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<TjHlwyyYwl> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time", map.get("endTime"));
        }

        wrapper.apply("KSBM IN (SELECT d.`WSJDM` FROM TB_DIC_Department d WHERE d.`KSTYBZ` = 1)");
        wrapper.apply("JKDADYCS + ZXZRC + ZXYYRC + ZXGHRC + FZZLRC + DZCFZS + JYSQDS + JCSQDS + HLFWRC > 0");
        wrapper.orderByDesc("YWSJ");
        wrapper.orderByDesc("create_time");
        IPage<TjHlwyyYwl> iPage = tjHlwyyYwlMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void tjHlwyyYwlOutExecl(Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<TjHlwyyYwl> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time", map.get("endTime"));
        }
        wrapper.apply("KSBM IN (SELECT d.`WSJDM` FROM TB_DIC_Department d WHERE d.`KSTYBZ` = 1)");
        wrapper.apply("JKDADYCS + ZXZRC + ZXYYRC + ZXGHRC + FZZLRC + DZCFZS + JYSQDS + JCSQDS + HLFWRC > 0");
        wrapper.orderByDesc("YWSJ");
        wrapper.orderByDesc("create_time");
        List<TjHlwyyYwl> tjHlwyyYwls = this.tjHlwyyYwlMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(tjHlwyyYwls)) {
            throw new BizException("当前无业务量统计表数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "业务量统计表格.xls";
        ExcelUtils.writeExcel(request, response, tjHlwyyYwls, TjHlwyyYwl.class, fileName);
    }
}