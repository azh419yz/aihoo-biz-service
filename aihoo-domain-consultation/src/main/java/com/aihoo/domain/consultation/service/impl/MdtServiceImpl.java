package com.aihoo.domain.consultation.service.impl;

import com.aihoo.domain.consultation.service.MdtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aihoo.domain.consultation.model.entity.Mdt;
import org.springframework.stereotype.Service;
import com.aihoo.domain.consultation.model.mapper.MdtMapper;

import java.util.List;

@Service
public class MdtServiceImpl extends ServiceImpl<MdtMapper, Mdt> implements MdtService {
    @Override
    public Mdt findMdtById(Integer id) {
        return baseMapper.selectById(id);
    }
    @Override
    public List<Mdt> findMdtAll() {
        return baseMapper.selectList(null);
    }
}
