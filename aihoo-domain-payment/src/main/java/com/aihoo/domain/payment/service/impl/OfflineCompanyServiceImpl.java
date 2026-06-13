package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.OfflineCompany;
import com.aihoo.domain.payment.model.mapper.OfflineCompanyMapper;
import com.aihoo.domain.payment.service.OfflineCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflineCompanyServiceImpl extends ServiceImpl<OfflineCompanyMapper, OfflineCompany> implements OfflineCompanyService {
}
