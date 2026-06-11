package com.aihoo.domain.im.service;

import com.aihoo.common.PageResult;
import com.aihoo.domain.im.model.vo.MdtPushMessageVo;
import com.aihoo.domain.im.model.entity.HomepageMessage;
import com.aihoo.domain.im.model.entity.PushMessage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发布信息表 服务类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
public interface MessageService {

    void homePageMessageAdd(Map<String, Object> map);

    Boolean homePageMessageUpdate(Map<String, Object> map);

    void pushMessageAdd(Map<String, Object> map);

    Boolean homePageMessageEnableDisable(Map<String, Object> map);

    Boolean pushMessageUpdate(Map<String, Object> map);

    Boolean pushMessageEnableDisable(Map<String, Object> map);

    List<HomepageMessage> homePageMessageList();

    PageResult<PushMessage> pushMessageList(Map<String, Object> map);

    PageResult<MdtPushMessageVo> mdtMessageList(Map<String, Object> map);

    void read(String ids);

    void delete(String ids);
}
