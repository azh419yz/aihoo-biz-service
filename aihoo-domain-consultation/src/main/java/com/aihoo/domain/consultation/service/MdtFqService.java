package com.aihoo.domain.consultation.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.consultation.model.vo.MdtFqVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * MDT 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-09-21
 */
public interface MdtFqService extends IService<MdtFqVo> {

    PageResult<MdtFqVo> fuzzQueryMdt(Map<String, Object> map);
}