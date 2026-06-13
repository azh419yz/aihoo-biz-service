package com.aihoo.domain.payment.service.impl;

import com.aihoo.domain.payment.model.entity.OfflineBlacklist;
import com.aihoo.domain.payment.model.mapper.OfflineBlacklistMapper;
import com.aihoo.domain.payment.service.OfflineBlacklistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OfflineBlacklistServiceImpl extends ServiceImpl<OfflineBlacklistMapper, OfflineBlacklist> implements OfflineBlacklistService {
}
