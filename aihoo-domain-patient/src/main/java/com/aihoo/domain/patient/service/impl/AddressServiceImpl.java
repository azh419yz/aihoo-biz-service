package com.aihoo.domain.patient.service.impl;


import com.aihoo.domain.patient.model.entity.Address;
import com.aihoo.domain.patient.model.mapper.AddressMapper;
import com.aihoo.domain.patient.service.AddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 患者收货地址 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-10-08
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

}
