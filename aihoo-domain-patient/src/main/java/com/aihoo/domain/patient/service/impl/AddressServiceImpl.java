package com.aihoo.domain.patient.service.impl;

import com.aihoo.domain.patient.model.entity.Address;
import com.aihoo.domain.patient.model.mapper.AddressMapper;
import com.aihoo.domain.patient.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
}
