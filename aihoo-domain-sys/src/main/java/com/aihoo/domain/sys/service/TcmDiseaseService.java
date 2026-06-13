package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.TcmDiseaseListReq;
import com.aihoo.domain.sys.dto.TcmDiseaseVo;
import com.aihoo.domain.sys.model.entity.TcmDisease;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 中医辨病服务接口
 */
public interface TcmDiseaseService extends IService<TcmDisease> {

    /**
     * 获取疾病列表
     */
    List<TcmDiseaseVo> getDiseaseList(TcmDiseaseListReq req);
}
