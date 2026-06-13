package com.aihoo.domain.prescription.service.impl;

import com.aihoo.domain.prescription.model.entity.HosPreDrugOrder;
import com.aihoo.domain.prescription.model.mapper.HosPreDrugMapper;
import com.aihoo.domain.prescription.service.HosPreDrugService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HosPreDrugServiceImpl extends ServiceImpl<HosPreDrugMapper, HosPreDrugOrder> implements HosPreDrugService {
}
