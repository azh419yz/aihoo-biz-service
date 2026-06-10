package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.LoginRecord;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface LoginRecordService extends IService<LoginRecord> {

    PageResult<LoginRecord> getList(Map<String, Object> map);
}