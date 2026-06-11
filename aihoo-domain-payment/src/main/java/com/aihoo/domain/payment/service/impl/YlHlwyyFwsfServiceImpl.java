package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlHlwyyFwsf;
import com.aihoo.domain.payment.model.mapper.YlHlwyyFwsfMapper;
import com.aihoo.domain.payment.service.YlHlwyyFwsfService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 互联网服务收费表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Service
public class YlHlwyyFwsfServiceImpl extends ServiceImpl<YlHlwyyFwsfMapper, YlHlwyyFwsf> implements YlHlwyyFwsfService {


    @Autowired
    private YlHlwyyFwsfMapper ylHlwyyFwsfMapper;

    @Override
    public PageResult<YlHlwyyFwsf> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<YlHlwyyFwsf> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("STFSJ");
        wrapper.orderByDesc("create_time");
        IPage<YlHlwyyFwsf> iPage = ylHlwyyFwsfMapper.selectPage(new Page<>(page, limit), wrapper);
        resetDateTime(iPage.getRecords());
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void ylHlwyyFwsfOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<YlHlwyyFwsf> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("STFSJ");
        wrapper.orderByDesc("create_time");
        List<YlHlwyyFwsf> ylHlwyyFwsfs = this.ylHlwyyFwsfMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(ylHlwyyFwsfs)) {
            throw new BizException("当前无服务收费表数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "服务收费表格.xls";
        resetDateTime(ylHlwyyFwsfs);
        ExcelUtils.writeExcel(request, response, ylHlwyyFwsfs, YlHlwyyFwsf.class, fileName);
    }

    private void resetDateTime(List<YlHlwyyFwsf> list){
        list.forEach(x->{
            x.setStfsj(StringUtil.getDateTimes(x.getStfsj()));
        });
    }
}