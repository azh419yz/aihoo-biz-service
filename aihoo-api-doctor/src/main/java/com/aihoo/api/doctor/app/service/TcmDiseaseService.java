package com.aihoo.api.doctor.app.service;

import com.aihoo.domain.sys.model.entity.TcmDisease;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.request.TcmDiseaseListReq;
import com.aihoo.api.doctor.app.controller.vo.TcmDiseaseVo;

import java.util.List;

/**
 * 中医辨病服务接口
 */
public interface TcmDiseaseService extends IService<TcmDisease> {

    /**
     * 获取疾病列表
     *
     * @param req 请求参数
     * @return 疾病视图对象列表
     */
    List<TcmDiseaseVo> getDiseaseList(TcmDiseaseListReq req);
}
