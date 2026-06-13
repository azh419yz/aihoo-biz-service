package com.aihoo.domain.consultation.service.impl;

import com.aihoo.domain.consultation.model.entity.Dicom;
import com.aihoo.domain.consultation.model.mapper.DicomMapper;
import com.aihoo.domain.consultation.service.DicomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DicomServiceImpl extends ServiceImpl<DicomMapper, Dicom> implements DicomService {
}
