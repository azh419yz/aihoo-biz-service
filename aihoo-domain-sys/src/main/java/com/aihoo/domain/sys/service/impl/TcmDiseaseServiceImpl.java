package com.aihoo.domain.sys.service.impl;

import com.aihoo.domain.sys.dto.TcmDiseaseListReq;
import com.aihoo.domain.sys.dto.TcmDiseaseVo;
import com.aihoo.domain.sys.model.entity.TcmDisease;
import com.aihoo.domain.sys.model.mapper.TcmDiseaseMapper;
import com.aihoo.domain.sys.service.TcmDiseaseService;
import com.aihoo.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 中医辨病服务实现类
 */
@Service("tcmDiseaseServiceImpl")
public class TcmDiseaseServiceImpl extends ServiceImpl<TcmDiseaseMapper, TcmDisease> implements TcmDiseaseService {

    @Override
    public List<TcmDiseaseVo> getDiseaseList(TcmDiseaseListReq req) {
        LambdaQueryWrapper<TcmDisease> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(TcmDisease::getStatus, 1);

        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            String keyword = req.getKeyword();
            queryWrapper.and(wrapper ->
                wrapper.like(TcmDisease::getDiseaseName, keyword)
                       .or()
                       .like(TcmDisease::getDiseasePinyinInitial, keyword.toUpperCase())
            );
        }

        queryWrapper.orderByAsc(TcmDisease::getSortOrder);

        List<TcmDisease> list = this.list(queryWrapper);

        return list.stream().map(disease -> {
            TcmDiseaseVo vo = new TcmDiseaseVo();
            BeanUtils.copyProperties(disease, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
