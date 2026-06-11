package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlDzcfmx;
import com.aihoo.domain.payment.model.mapper.YlDzcfmxMapper;
import com.aihoo.domain.payment.service.YlDzcfmxService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.util.StringUtil;
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
 * 电子处方明细表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-28
 */
@Service
@Slf4j
public class YlDzcfmxServiceImpl extends ServiceImpl<YlDzcfmxMapper, YlDzcfmx> implements YlDzcfmxService {

    @Autowired
    private YlDzcfmxMapper ylDzcfmxMapper;

    @Override
    public PageResult<YlDzcfmx> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<YlDzcfmx> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("KFSJ");
        wrapper.orderByDesc("create_time");
        IPage<YlDzcfmx> iPage = ylDzcfmxMapper.selectPage(new Page<>(page, limit), wrapper);
        resetDateTime(iPage.getRecords());
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void ylDzcfmxOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<YlDzcfmx> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("KFSJ");
        wrapper.orderByDesc("create_time");
        List<YlDzcfmx> ylDzcfmxes = this.ylDzcfmxMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(ylDzcfmxes)) {
            throw new BizException("当前无电子处方明细表数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "电子处方明细表格.xls";
        resetDateTime(ylDzcfmxes);
        ExcelUtils.writeExcel(request, response, ylDzcfmxes, YlDzcfmx.class, fileName);
    }
    private void resetDateTime(List<YlDzcfmx> list){
        list.forEach(x->{
            x.setKfsj(StringUtil.getDateTimes(x.getKfsj()));
        });
    }
}