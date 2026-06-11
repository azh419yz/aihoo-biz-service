package com.aihoo.domain.im.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aihoo.common.PageResult;
import com.aihoo.constant.UserRoleEnum;
import com.aihoo.domain.im.model.vo.MdtPushMessageVo;
import com.aihoo.domain.im.model.entity.HomepageMessage;
import com.aihoo.domain.im.model.entity.PushMessage;
import com.aihoo.domain.im.model.mapper.MessageMapper;
import com.aihoo.domain.im.model.mapper.PushMessageMapper;
import com.aihoo.domain.im.service.MessageService;
import com.aihoo.domain.doctor.model.entity.DoctorUser;
import com.aihoo.domain.doctor.model.mapper.DoctorUserMapper;
import com.aihoo.domain.sys.model.entity.SysUser;
import com.aihoo.domain.sys.model.mapper.SysUserMapper;
import com.aihoo.exception.BizException;
import com.aihoo.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    @Resource
    private DoctorUserMapper doctorUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    // SysUserRoleService 暂未在 aihoo-domain-sys 提供，沿用 MdtServiceImpl 的 ApplicationContext 兜底
    private Object sysUserRoleService;

    @Autowired
    private ApplicationContext applicationContext;

    private Object getBeanIfPresent(String name) {
        try {
            return applicationContext.containsBean(name) ? applicationContext.getBean(name) : null;
        } catch (Exception e) {
            return null;
        }
    }

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
        // 旧代码使用 SecurityUtils.getLoginUser()，但 shared-kernel 仅提供 getLoginUserId()。
        // 此处按 loginUserId 从 sysUserMapper 查 SysUser 以获取 idCard。
        SysUser user = sysUserMapper.selectById(SecurityUtils.getLoginUserId());
        List<String> roles;
        if (sysUserRoleService == null) {
            sysUserRoleService = getBeanIfPresent("sysUserRoleService");
        }
        if (sysUserRoleService != null) {
            // 反射调用 userRole()（SysUserRoleService 暂未迁移到 domain-sys）
            try {
                roles = (List<String>) sysUserRoleService.getClass().getMethod("userRole").invoke(sysUserRoleService);
            } catch (Exception e) {
                roles = java.util.Collections.emptyList();
            }
        } else {
            roles = java.util.Collections.emptyList();
        }
        //会诊助理医生角色
        boolean hzzlys = roles.contains(UserRoleEnum.HZZLYS.getCode());
        //医疗管家角色
        boolean ylgj = roles.contains(UserRoleEnum.YLGJ.getCode());
        if (hzzlys) {
            type = "DOCKER_PERSONAL";
            String idCard = user == null ? null : user.getIdCard();
            LambdaQueryWrapper<DoctorUser> doctorUserLambdaQueryWrapper = new QueryWrapper<DoctorUser>().lambda();
            doctorUserLambdaQueryWrapper.eq(DoctorUser::getPapersNumbers, idCard);
            doctorUserLambdaQueryWrapper.eq(DoctorUser::getIsCancel, "0");
            DoctorUser doctorUser = doctorUserMapper.selectOne(doctorUserLambdaQueryWrapper);
            otherId = doctorUser == null ? null : doctorUser.getId();
        } else if (ylgj) {
            type = "ADMIN_PERSONAL";
            otherId = user == null ? null : user.getId();
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
