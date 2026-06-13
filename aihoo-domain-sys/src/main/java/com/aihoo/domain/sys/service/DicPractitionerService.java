package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.DicPractitioner;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 医务人员字典表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-10-26
 */
public interface DicPractitionerService extends IService<DicPractitioner> {

    PageResult<DicPractitioner> page(Map<String,Object> map);

    void dicPractitionerOutExecl(Map<String,Object> map, HttpServletRequest request, HttpServletResponse response);

}
