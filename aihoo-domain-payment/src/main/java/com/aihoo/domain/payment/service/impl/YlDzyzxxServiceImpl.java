package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlDzyzxx;
import com.aihoo.domain.payment.model.mapper.YlDzyzxxMapper;
import com.aihoo.domain.payment.service.YlDzyzxxService;
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
 * 电子医嘱信息表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-28
 */
@Service
@Slf4j
public class YlDzyzxxServiceImpl extends ServiceImpl<YlDzyzxxMapper, YlDzyzxx> implements YlDzyzxxService {

    @Autowired
    private YlDzyzxxMapper ylDzyzxxMapper;


    @Override
    public PageResult<YlDzyzxx> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<YlDzyzxx> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("YZKJSJ");
        wrapper.orderByDesc("create_time");
        IPage<YlDzyzxx> iPage = ylDzyzxxMapper.selectPage(new Page<>(page, limit), wrapper);
        resetDateTime(iPage.getRecords());
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void ylDzyzxxOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<YlDzyzxx> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("YZKJSJ");
        wrapper.orderByDesc("create_time");
        List<YlDzyzxx> ylDzyzxxes = this.ylDzyzxxMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(ylDzyzxxes)) {
            throw new BizException("当前无电子医嘱信息表数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "电子医嘱信息表格.xls";
        resetDateTime(ylDzyzxxes);
        ExcelUtils.writeExcel(request, response, ylDzyzxxes, YlDzyzxx.class, fileName);
    }

    private void resetDateTime(List<YlDzyzxx> list){
        list.forEach(x->{
            x.setYzkjsj(StringUtil.getDateTimes(x.getYzkjsj()));
        });
    }
}