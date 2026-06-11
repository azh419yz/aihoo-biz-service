package com.aihoo.domain.payment.service.impl;

import com.aihoo.common.PageResult;
import com.aihoo.domain.payment.model.entity.YlGhdxx;
import com.aihoo.domain.payment.model.mapper.YlGhdxxMapper;
import com.aihoo.domain.payment.service.YlGhdxxService;
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
 * 挂号单信息表 服务实现类（依赖 patient 域 PatientUser）
 * </p>
 *
 * <p>TODO: 跨域依赖 patient 域的 PatientUserMapper / PatientUser，目前 stub。
 * 等 patient 域迁入后恢复 resetDateTime() 中的患者实名信息查询。</p>
 *
 * @author lx
 * @since 2020-10-27
 */
@Service
@Slf4j
public class YlGhdxxServiceImpl extends ServiceImpl<YlGhdxxMapper, YlGhdxx> implements YlGhdxxService {

    @Autowired
    private YlGhdxxMapper ylGhdxxMapper;

    // TODO: 跨域依赖，等 patient 域迁入后再注入
    // @Autowired
    // private PatientUserMapper patientUserMapper;

    @Override
    public PageResult<YlGhdxx> page(Map<String, Object> map) {
        long page = 1;
        long limit = 10;

        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        QueryWrapper<YlGhdxx> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("YLJGDM");
        wrapper.orderByDesc("create_time");
        IPage<YlGhdxx> iPage = ylGhdxxMapper.selectPage(new Page<>(page, limit), wrapper);
        resetDateTime(iPage.getRecords());
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }


    @Override
    public void ylGhdxxOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<YlGhdxx> wrapper = new QueryWrapper<>();
        if (null != map.get("startTime") && !"".equals(map.get("startTime").toString())) {
            wrapper.ge("create_time", map.get("startTime"));
        }
        if (null != map.get("endTime") && !"".equals(map.get("endTime").toString())) {
            wrapper.le("create_time",map.get("endTime"));
        }
        wrapper.orderByDesc("YLJGDM");
        wrapper.orderByDesc("create_time");
        List<YlGhdxx> ylGhdxxes = this.ylGhdxxMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(ylGhdxxes)) {
            throw new BizException("当前无挂号单信息表数据");
        }
        String fileName = DateUtil.getSimpleDateFormat(DateUtil.DATE_FORMAT_2).format(new Date()) + "挂号单信息表格.xls";
        resetDateTime(ylGhdxxes);
        ExcelUtils.writeExcel(request, response, ylGhdxxes, YlGhdxx.class, fileName);
    }

    /**
     * 重置认证时间/认证日期
     *
     * TODO: 跨域依赖 patient 域 PatientUser - 等 patient 域迁入后恢复
     */
    private void resetDateTime(List<YlGhdxx> ylGhdxxes){
        if (ylGhdxxes == null || ylGhdxxes.isEmpty()) {
            return;
        }
        // TODO: 跨域 stub - 等 patient 域迁入后恢复
        // Set<String> idCardSet = ylGhdxxes.stream().map(x->x.getSfzjhm()).collect(Collectors.toSet());
        // if(idCardSet.size() > 0){
        //     QueryWrapper<PatientUser> qw = new QueryWrapper<PatientUser>();
        //     qw.in("id_card", idCardSet).orderByDesc("status").orderByDesc("FIND_IN_SET(is_auth,'REJECT,PASS')");
        //     Map<String,String> idMap = patientUserMapper.selectList(qw).stream().collect(Collectors.toMap(x->x.getIdCard(),x->StringUtil.getStr(x.getAuthTime()).replaceAll("-",""),(x,y)->x));
        //     ylGhdxxes.forEach(x->{
        //         x.setAuthTime(idMap.get(x.getSfzjhm()));
        //         x.setDyrq(StringUtil.getDateTimes(x.getDyrq()));
        //     });
        // }
        ylGhdxxes.forEach(x -> x.setDyrq(StringUtil.getDateTimes(x.getDyrq())));
    }
}