package com.aihoo.domain.im.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.im.model.entity.ImCustomerMsg;
import com.aihoo.domain.im.model.vo.CustomerVo;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.alibaba.fastjson2.JSONArray;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Classname CustomerService
 * @Description hf
 * @Date 2020/11/10 13:19
 * @Created by ad
 */
public interface CustomerService {
    PageResult<SysUser> customerList(Map<String, Object> map);

    PageResult<CustomerVo> customerDetails(Map<String, Object> map) throws ParseException;

    List<ImCustomerMsg> chattingRecords(Map<String, Object> map);

    List search(Map<String, Object> map);

    JSONArray mdtList();
}
