package com.aihoo.domain.consultation.service;

import com.aihoo.domain.consultation.model.entity.Mdt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Stub service for consultation/Mdt.
 * TODO: Real migration will populate this interface.
 */
public interface MdtService extends IService<Mdt> {
    Mdt findMdtById(Integer id);
    List<Mdt> findMdtAll();
}
