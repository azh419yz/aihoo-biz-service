package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.dto.TcmSyndromeListReq;
import com.aihoo.domain.sys.dto.TcmSyndromeVo;
import com.aihoo.domain.sys.model.entity.TcmSyndrome;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 中医辨证服务接口
 */
public interface TcmSyndromeService extends IService<TcmSyndrome> {

    /**
     * 获取证候列表
     */
    List<TcmSyndromeVo> getSyndromeList(TcmSyndromeListReq req);
}
