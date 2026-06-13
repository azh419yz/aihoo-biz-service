package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunjianbo
 * @since 2019-07-17
 */
public interface LoginRecordService extends IService<LoginRecord> {

     PageResult<LoginRecord> getList(Map<String, Object> map, HttpServletRequest request);

     void addLoginRecord(String userId, HttpServletRequest request);
}
