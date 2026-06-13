package com.aihoo.domain.sys.service;

import com.aihoo.domain.im.model.entity.ImCustomerMsg;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.common.PageResult;
import com.alibaba.fastjson2.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @Classname CustomerService
 * @Description 客服 service - sys 域（系统用户管理客服）
 *
 * 注：业务上混合了 sys 域（系统用户管理）和 im 域（聊天消息）。
 * customerDetails() 返回 Object 避免依赖 api-admin 的 vo 包，由 controller 自行 cast。
 * @Date 2020/11/10 13:19
 * @Created by ad
 */
public interface CustomerService {
    PageResult<SysUser> customerList(Map<String, Object> map);

    Object customerDetails(Map<String, Object> map) throws Exception;

    List<ImCustomerMsg> chattingRecords(Map<String, Object> map);

    List search(Map<String, Object> map);

    JSONArray mdtList();
}
