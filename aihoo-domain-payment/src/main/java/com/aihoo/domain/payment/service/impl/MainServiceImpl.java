package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.Main;
import com.aihoo.domain.payment.model.mapper.MainMapper;
import com.aihoo.domain.payment.service.MainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl extends ServiceImpl<MainMapper, Main> implements MainService {
}
