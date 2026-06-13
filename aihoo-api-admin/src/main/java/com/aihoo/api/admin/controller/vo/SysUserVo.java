package com.aihoo.api.admin.controller.vo;


import com.aihoo.domain.sys.model.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: sunjianbo
 * @date: 2019/5/17 14:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserVo extends SysUser {

    @Data
    public static class roleNames {
        private String roleName;
        private String roleId;
    }

    //角色名称集合
    private List<roleNames> roleNames;

    private List<String> drugstoreIdList;
}
