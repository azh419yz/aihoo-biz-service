package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.mapper.SysMenuMapper;
import com.aihoo.domain.sys.model.mapper.SysRoleMenuMapper;
import com.aihoo.domain.sys.model.entity.SysMenu;
import com.aihoo.domain.sys.model.entity.SysRoleMenu;
import com.aihoo.domain.sys.service.SysRoleMenuService;
import com.aihoo.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRoleAuth(Integer roleId, List<Integer> authIds) {
        if (CollectionUtils.isEmpty(authIds)){
            // 传入的没有任何权限，直接删除之前绑定的所有权限
            baseMapper.delete(new UpdateWrapper<SysRoleMenu>().eq("role_id", roleId));
            return true;
        }
        //如果选中了子菜单默认父级菜单勾选，也就是添加父菜单id
        List<SysMenu> sysMenus = this.sysMenuMapper.selectList(new QueryWrapper<SysMenu>().in("id", authIds));
        List<String> parentIds = sysMenus.stream().map(SysMenu::getParentId).filter(s -> !"-1".equals(s)).distinct().collect(Collectors.toList());
        parentIds.forEach(s -> {
            if (!authIds.contains(Integer.valueOf(s))){
                authIds.add(Integer.valueOf(s));
            }
        });

        baseMapper.delete(new UpdateWrapper<SysRoleMenu>().eq("role_id", roleId));
        if (authIds != null && authIds.size() > 0) {
            if (baseMapper.insertRoleAuths(roleId, authIds) < authIds.size()) {
                throw new BizException("操作失败");
            }
        }
        return true;
    }
}
