package com.aihoo.domain.sys.service;

import com.aihoo.domain.sys.model.entity.SysMenu;
import com.aihoo.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
public interface SysMenuService extends IService<SysMenu> {

    List<Map<String, Object>> userMenuButton(Integer userId);

    List<SysMenu> listByUserId(Integer userId);

    List<SysMenu> listByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<SysMenu> listByRoleId(Integer roleId);
    
    
    //save,updateById,update
    
    boolean saveDto(SysMenu authorities);
    
    boolean updateByIdDto(SysMenu authorities);
    
    boolean updateDto(Map<String,Object> map);

    PageResult<SysMenu> getPage();
}
