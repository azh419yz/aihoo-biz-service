package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlZxfzjl;
import com.aihoo.domain.payment.model.mapper.YlZxfzjlMapper;
import com.aihoo.domain.payment.service.YlZxfzjlService;
import com.aihoo.exception.BizException;
import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.ConvertorTime;
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
 * 在线复诊记录表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Service
@Slf4j
public class YlZxfzjlServiceImpl extends ServiceImpl<YlZxfzjlMapper, YlZxfzjl> implements YlZxfzjlService {

    @Autowired
    private YlZxfzjlMapper ylZxfzjlMapper;


    @Override
    public PageResult<YlZxfzjl> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<YlZxfzjl> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("FZSQSJ");
        wrapper.orderByDesc("create_time");
        IPage<YlZxfzjl> iPage = ylZxfzjlMapper.selectPage(new Page<>(page, limit), wrapper);
        resetDateTime(iPage.getRecords());
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void ylZxfzjlOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<YlZxfzjl> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("FZSQSJ");
        wrapper.orderByDesc("create_time");
        List<YlZxfzjl> ylZxfzjls = this.ylZxfzjlMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(ylZxfzjls)) {
            throw new BizException("当前无在线复诊记录表数据");
        }
        resetDateTime(ylZxfzjls);
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "在线复诊记录表格.xls";
        ExcelUtils.writeExcel(request, response, ylZxfzjls, YlZxfzjl.class, fileName);
    }

    private void resetDateTime(List<YlZxfzjl> list){
        list.forEach(x->{
            x.setFzsqsj(StringUtil.getDateTimes(x.getFzsqsj()));
            x.setHzqdsj(StringUtil.getDateTimes(x.getHzqdsj()));
            x.setFzjzsj(StringUtil.getDateTimes(x.getFzjzsj()));
            x.setZlsc(ConvertorTime.secToTimes(StringUtil.getInteger(x.getZlsc())*60));
        });
    }

}