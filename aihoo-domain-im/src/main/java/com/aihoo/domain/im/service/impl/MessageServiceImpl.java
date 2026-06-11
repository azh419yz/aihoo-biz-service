package com.aihoo.domain.im.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.PageResult;
import com.aihoo.domain.im.model.vo.MdtPushMessageVo;
import com.aihoo.domain.im.model.entity.HomepageMessage;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.aihoo.domain.im.model.mapper.MessageMapper;
import com.aihoo.domain.im.model.mapper.PushMessageMapper;
import com.aihoo.domain.im.service.MessageService;
import com.aihoo.exception.BizException;
import com.aihoo.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 发布信息表 服务实现类
 * </p>
 *
 * @author mcp
 * @since 2020-08-10
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private PushMessageMapper pushMessageMapper;

    // TODO: 跨域依赖，待 aihoo-domain-doctor 迁移后改用其 DoctorUserMapper
    // @Resource
    // private DoctorUserMapper doctorUserMapper;

    // TODO: 跨域依赖 SysUserRoleService（未在 aihoo-domain-sys 提供），旧代码用其 userRole() 判定当前用户角色
    // @Resource
    // private SysUserRoleService sysUserRoleService;

    // 角色枚举常量（与旧 com.aihoo.admin.common.constant.UserRoleEnum 保持一致）
    private static final String ROLE_HZZLYS = "18"; // 会诊/助理医生
    private static final String ROLE_YLGJ = "17";   // 医疗管家

    @Override
    public void homePageMessageAdd(Map<String, Object> map) {
        HomepageMessage homepageMessage = new HomepageMessage();
        homepageMessage.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        homepageMessage.setTitle(map.get("title").toString());
        homepageMessage.setType(map.get("type").toString());
        this.messageMapper.insert(homepageMessage);
    }

    @Override
    public Boolean homePageMessageUpdate(Map<String, Object> map) {
        HomepageMessage homepageMessage = new HomepageMessage();
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            homepageMessage.setType(map.get("type").toString());
        }
        if (null != map.get("title") && !"".equals(map.get("title"))) {
            homepageMessage.setTitle(map.get("title").toString());
        }
        homepageMessage.setId(map.get("id").toString());
        int i = this.messageMapper.updateById(homepageMessage);
        return i > 0;
    }

    @Override
    public void pushMessageAdd(Map<String, Object> map) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setContent(map.get("content").toString().trim());
        pushMessage.setCreateUserId(Objects.requireNonNull(SecurityUtils.getLoginUserId()).toString());
        pushMessage.setIntro(map.get("intro").toString().trim());
        pushMessage.setTitle(map.get("title").toString());
        pushMessage.setType(map.get("type").toString());
        pushMessage.setIsImg(map.get("isImg").toString());
        if ("1".equals(map.get("isImg"))) {
            // 有封面图
            pushMessage.setImg(map.get("img").toString());
        }
        //图片消息类型后台管理默认为图文公告
        pushMessage.setMessageType("IMAGE_TEXT");
        this.pushMessageMapper.insert(pushMessage);
    }

    @Override
    public Boolean homePageMessageEnableDisable(Map<String, Object> map) {
        HomepageMessage homepageMessage = new HomepageMessage();
        homepageMessage.setId(map.get("id").toString());
        homepageMessage.setIsDelete(map.get("isDelete").toString());
        int i = this.messageMapper.updateById(homepageMessage);
        return i > 0;
    }

    @Override
    public Boolean pushMessageUpdate(Map<String, Object> map) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setId(map.get("id").toString());
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            pushMessage.setType(map.get("type").toString());
        }
        if (null != map.get("title") && !"".equals(map.get("title"))) {
            pushMessage.setTitle(map.get("title").toString().trim());
        }
        if (null != map.get("intro") && !"".equals(map.get("intro"))) {
            pushMessage.setIntro(map.get("intro").toString());
        }
        if (null != map.get("img") && !"".equals(map.get("img"))) {
            pushMessage.setImg(map.get("img").toString());
        }
        if (null != map.get("isImg") && !"".equals(map.get("isImg"))) {
            if ("0".equals(map.get("isImg"))) {
                //当修改没有图片，需要将原来的存在的图片设置为空
                pushMessage.setImg("");
            }
            pushMessage.setIsImg(map.get("isImg").toString());
        }
        if (null != map.get("content") && !"".equals(map.get("content"))) {
            pushMessage.setContent(map.get("content").toString().trim());
        }
        int i = this.pushMessageMapper.updateById(pushMessage);
        return i > 0;
    }

    @Override
    public Boolean pushMessageEnableDisable(Map<String, Object> map) {
        PushMessage pushMessage = new PushMessage();
        pushMessage.setId(map.get("id").toString());
        pushMessage.setIsDelete(map.get("isDelete").toString());
        int i = this.pushMessageMapper.updateById(pushMessage);
        return i > 0;
    }

    @Override
    public List<HomepageMessage> homePageMessageList() {
        QueryWrapper<HomepageMessage> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return this.messageMapper.selectList(wrapper);
    }

    @Override
    public PageResult<PushMessage> pushMessageList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        QueryWrapper<PushMessage> wrapper = new QueryWrapper<>();
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        if (null != map.get("type") && !"".equals(map.get("type"))) {
            wrapper.eq("type", map.get("type"));
        }
        wrapper.in("type", Arrays.asList("ALL", "PATIENT", "DOCKER"));
        IPage<PushMessage> iPage = this.pushMessageMapper.selectPage(new Page<>(page, limit), wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public PageResult<MdtPushMessageVo> mdtMessageList(Map<String, Object> map) {
        long page = 1;
        long limit = 10;
        String orderNum = "";
        String startDate = "";
        String endDate = "";
        String content = "";
        String type = "";
        String otherId = "";
        // TODO: 跨域依赖 SysUser，旧代码通过 SecurityUtils.getLoginUser() 获取当前登录用户
        // 当前域内 shared-kernel SecurityUtils.getLoginUser() 未提供（仅有 getLoginUserId()），
        // 暂时根据 ID 自行判断角色
        // SysUser user = SecurityUtils.getLoginUser();
        // List<String> roles = sysUserRoleService.userRole();
        // boolean hzzlys = roles.contains(ROLE_HZZLYS);
        // boolean ylgj = roles.contains(ROLE_YLGJ);
        // 旧逻辑：
        //   if (hzzlys) { type = "DOCKER_PERSONAL"; ... doctorUserMapper ... }
        //   else if (ylgj) { type = "ADMIN_PERSONAL"; otherId = user.getId(); }
        // TODO: 待 aihoo-domain-sys 提供 SysUserRoleService.userRole() 和 DoctorUser 域后恢复
        boolean hzzlys = false;
        boolean ylgj = false;
        if (hzzlys) {
            type = "DOCKER_PERSONAL";
            // TODO: 跨域依赖 DoctorUser（aihoo-domain-doctor 待迁移）
            // String idCard = user.getIdCard();
            // LambdaQueryWrapper<DoctorUser> wrapper = new QueryWrapper<DoctorUser>().lambda();
            // wrapper.eq(DoctorUser::getPapersNumbers, idCard);
            // wrapper.eq(DoctorUser::getIsCancel, "0");
            // DoctorUser doctorUser = doctorUserMapper.selectOne(wrapper);
            // otherId = doctorUser.getId();
            otherId = "";
        } else if (ylgj) {
            type = "ADMIN_PERSONAL";
            // TODO: 跨域依赖 SysUser.getId()（aihoo-domain-sys 已迁移）
            otherId = SecurityUtils.getLoginUserId();
        }
        if (null != map.get("page") && !"".equals(map.get("page"))) {
            page = Long.parseLong(map.get("page").toString());
        }
        if (null != map.get("limit") && !"".equals(map.get("limit"))) {
            limit = Long.parseLong(map.get("limit").toString());
        }
        if (null != map.get("orderNum") && !"".equals(map.get("orderNum"))) {
            orderNum = map.get("orderNum").toString();
        }
        if (null != map.get("content") && !"".equals(map.get("content"))) {
            content = "%" + map.get("content").toString() + "%";
        }
        if (null != map.get("date") && !"".equals(map.get("date"))) {
            String date = map.get("date").toString();
            String[] split = date.split(" - ");
            if (split.length == 2 && StrUtil.isNotBlank(split[0]) && StrUtil.isNotBlank(split[1])) {
                DateTime parse1 = DateUtil.parse(split[0]);
                DateTime parse2 = DateUtil.parse(split[1]);
                DateTime dateTime1 = DateUtil.beginOfDay(parse1);
                DateTime dateTime2 = DateUtil.endOfDay(parse2);
                startDate = dateTime1.toStringDefaultTimeZone();
                endDate = dateTime2.toStringDefaultTimeZone();
            }
        }
        IPage<MdtPushMessageVo> list = this.pushMessageMapper.mdtMessageList(new Page<>(page, limit), otherId, type, "MDT", orderNum, startDate, endDate, content);
        return new PageResult<>(list.getRecords(), list.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void read(String ids) {
        String[] split = ids.split(",");
        PushMessage pushMessage = new PushMessage();
        pushMessage.setIsRead("1");
        LambdaQueryWrapper<PushMessage> lambda = new QueryWrapper<PushMessage>().lambda();
        lambda.in(PushMessage::getId, (Object) split);
        int update = this.pushMessageMapper.update(pushMessage, lambda);
        if (update == 0) {
            throw new BizException("已读操作失败");
        }
    }

    @Override
    public void delete(String ids) {
        String[] split = ids.split(",");
        PushMessage pushMessage = new PushMessage();
        pushMessage.setIsDelete("1");
        LambdaQueryWrapper<PushMessage> lambda = new QueryWrapper<PushMessage>().lambda();
        lambda.in(PushMessage::getId, (Object) split);
        int update = this.pushMessageMapper.update(pushMessage, lambda);
        if (update == 0) {
            throw new BizException("删除操作失败");
        }
    }
}
