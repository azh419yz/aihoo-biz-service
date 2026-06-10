package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> listByUserId(Integer userId);

    List<SysMenu> listByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<SysMenu> listByRoleId(Integer roleId);
}
