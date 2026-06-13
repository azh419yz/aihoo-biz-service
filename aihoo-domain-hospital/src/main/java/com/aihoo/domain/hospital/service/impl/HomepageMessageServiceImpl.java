package com.aihoo.domain.hospital.service.impl;

import com.aihoo.domain.hospital.service.HomepageMessageService;
import com.aihoo.domain.hospital.model.entity.HomepageMessage;
import org.springframework.stereotype.Service;
import com.aihoo.domain.hospital.model.mapper.HomepageMessageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class HomepageMessageServiceImpl extends ServiceImpl<HomepageMessageMapper, HomepageMessage> implements HomepageMessageService {
}
