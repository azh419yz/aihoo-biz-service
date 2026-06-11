package com.aihoo.api.doctor.app.service.impl;

import com.aihoo.util.StringUtil;
import com.aihoo.domain.sys.model.mapper.TcmDiseaseMapper;
import com.aihoo.domain.sys.model.entity.TcmDisease;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.api.doctor.app.controller.request.TcmDiseaseListReq;
import com.aihoo.api.doctor.app.controller.vo.TcmDiseaseVo;
import com.aihoo.api.doctor.app.service.TcmDiseaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 中医辨病服务实现类
 */
@Service
public class TcmDiseaseServiceImpl extends ServiceImpl<TcmDiseaseMapper, TcmDisease> implements TcmDiseaseService {

    @Override
    public List<TcmDiseaseVo> getDiseaseList(TcmDiseaseListReq req) {
        LambdaQueryWrapper<TcmDisease> queryWrapper = new LambdaQueryWrapper<>();
        
        // 状态（0-禁用 1-启用）
        queryWrapper.eq(TcmDisease::getStatus, 1);
        
        // 模糊搜索：疾病名称或拼音首字母
        if (req != null && StringUtil.isNotBlank(req.getKeyword())) {
            String keyword = req.getKeyword();
            queryWrapper.and(wrapper -> 
                wrapper.like(TcmDisease::getDiseaseName, keyword)
                       .or()
                       .like(TcmDisease::getDiseasePinyinInitial, keyword.toUpperCase())
            );
        }
        
        // 按排序序号排序
        queryWrapper.orderByAsc(TcmDisease::getSortOrder);
        
        List<TcmDisease> list = this.list(queryWrapper);
        
        // 转换为VO列表
        return list.stream().map(disease -> {
            TcmDiseaseVo vo = new TcmDiseaseVo();
            BeanUtils.copyProperties(disease, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
