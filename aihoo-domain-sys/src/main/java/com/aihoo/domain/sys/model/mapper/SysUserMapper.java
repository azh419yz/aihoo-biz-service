package com.aihoo.domain.sys.model.mapper;

import com.aihoo.domain.sys.model.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author sunjianbo
 * @since 2019-05-17
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectByUsername(String username);

    int isDelete(Integer id);

    IPage<SysUser> customerList(Page<SysUser> setPage, @Param("map") Map<String, Object> map);
}
