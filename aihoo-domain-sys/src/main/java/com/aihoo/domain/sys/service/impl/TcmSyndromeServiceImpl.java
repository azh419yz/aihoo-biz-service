package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.dto.TcmSyndromeListReq;
import com.aihoo.domain.sys.dto.TcmSyndromeVo;
import com.aihoo.domain.sys.model.entity.TcmSyndrome;
import com.aihoo.domain.sys.model.mapper.TcmSyndromeMapper;
import com.aihoo.domain.sys.service.TcmSyndromeService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 中医辨证服务实现类
 */
@Service("tcmSyndromeServiceImpl")
public class TcmSyndromeServiceImpl extends ServiceImpl<TcmSyndromeMapper, TcmSyndrome> implements TcmSyndromeService {

    @Override
    public List<TcmSyndromeVo> getSyndromeList(TcmSyndromeListReq req) {
        LambdaQueryWrapper<TcmSyndrome> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(TcmSyndrome::getStatus, 1);

        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            queryWrapper.like(TcmSyndrome::getSyndromeName, req.getKeyword());
        }

        queryWrapper.orderByAsc(TcmSyndrome::getSortOrder);

        List<TcmSyndrome> list = this.list(queryWrapper);

        return list.stream().map(syndrome -> {
            TcmSyndromeVo vo = new TcmSyndromeVo();
            BeanUtils.copyProperties(syndrome, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
