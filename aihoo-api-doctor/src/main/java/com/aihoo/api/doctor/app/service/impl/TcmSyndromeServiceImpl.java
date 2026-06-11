package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.domain.sys.model.mapper.TcmSyndromeMapper;
import com.aihoo.domain.sys.model.entity.TcmSyndrome;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.request.TcmSyndromeListReq;
import com.aihoo.api.doctor.app.controller.vo.TcmSyndromeVo;
import com.aihoo.api.doctor.app.service.TcmSyndromeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 中医辨证服务实现类
 */
@Service
public class TcmSyndromeServiceImpl extends ServiceImpl<TcmSyndromeMapper, TcmSyndrome> implements TcmSyndromeService {

    @Override
    public List<TcmSyndromeVo> getSyndromeList(TcmSyndromeListReq req) {
        LambdaQueryWrapper<TcmSyndrome> queryWrapper = new LambdaQueryWrapper<>();

        // 状态（0-禁用 1-启用）
        queryWrapper.eq(TcmSyndrome::getStatus, 1);

        // 证候名称模糊匹配
        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            queryWrapper.like(TcmSyndrome::getSyndromeName, req.getKeyword());
        }

        // 按排序序号排序
        queryWrapper.orderByAsc(TcmSyndrome::getSortOrder);

        List<TcmSyndrome> list = this.list(queryWrapper);

        // 转换为VO列表
        return list.stream().map(syndrome -> {
            TcmSyndromeVo vo = new TcmSyndromeVo();
            BeanUtils.copyProperties(syndrome, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
