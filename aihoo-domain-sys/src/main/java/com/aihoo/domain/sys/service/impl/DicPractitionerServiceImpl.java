package com.aihoo.domain.sys.service.impl;

import com.aihoo.excel.ExcelUtils;
import com.aihoo.util.DateUtil;
import com.aihoo.domain.sys.model.entity.DicPractitioner;
import com.aihoo.domain.sys.model.mapper.DicPractitionerMapper;
import com.aihoo.domain.sys.service.DicPractitionerService;
import com.aihoo.common.PageResult;
import com.aihoo.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 医务人员字典表 服务实现类
 * </p>
 *
 * @author lx
 * @since 2020-10-26
 */
@Service
@Slf4j
public class DicPractitionerServiceImpl extends ServiceImpl<DicPractitionerMapper, DicPractitioner> implements DicPractitionerService {


    @Autowired
    private DicPractitionerMapper dicPractitionerMapper;


    @Override
    public PageResult<DicPractitioner> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<DicPractitioner> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.apply("LENGTH(IFNULL(CAZSXLM,'')) > 0");
        wrapper.orderByDesc("create_time");
        IPage<DicPractitioner> iPage = dicPractitionerMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void dicPractitionerOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<DicPractitioner> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.apply("LENGTH(IFNULL(CAZSXLM,'')) > 0");
        wrapper.orderByDesc("create_time");
        List<DicPractitioner> practitioners = this.dicPractitionerMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(practitioners)) {
            throw new BizException("当前无医务人员字典数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "医务人员字典表格.xls";
        ExcelUtils.writeExcel(request, response, practitioners, DicPractitioner.class, fileName);
    }
}
