package com.aihoo.api.doctor.app.service;

import com.aihoo.domain.sys.model.entity.TcmSyndrome;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aihoo.api.doctor.app.controller.request.TcmSyndromeListReq;
import com.aihoo.api.doctor.app.controller.vo.TcmSyndromeVo;

import java.util.List;

/**
 * 中医辨证服务接口
 */
public interface TcmSyndromeService extends IService<TcmSyndrome> {

    /**
     * 获取证候列表
     *
     * @param req 请求参数
     * @return 证候视图对象列表
     */
    List<TcmSyndromeVo> getSyndromeList(TcmSyndromeListReq req);
}
