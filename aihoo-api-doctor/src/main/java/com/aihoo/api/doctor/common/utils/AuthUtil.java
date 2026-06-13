package com.aihoo.api.doctor.common.utils;

import com.aihoo.security.LoginUser;

/**
 * 兼容垫片：原 AuthUtil 全部委托到 sk 的 com.aihoo.security.AuthUtil。
 * 任务 #56 阶段将删除此垫片，所有调用方应改用 com.aihoo.security.AuthUtil。
 */
public class AuthUtil {
    public static void clear() {
        com.aihoo.security.AuthUtil.clear();
    }

    public static void setLoginUser(LoginUser user) {
        com.aihoo.security.AuthUtil.setLoginUser(user);
    }

    /** 旧版：接受 DoctorUser。会构造 LoginUser。 */
    public static void setLoginUser(com.aihoo.domain.doctor.model.entity.DoctorUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setName(user.getName());
        loginUser.setType("DOCTOR");
        com.aihoo.security.AuthUtil.setLoginUser(loginUser);
    }

    /** 旧版：接受 Object（兼容历史调用）。 */
    public static void setLoginUser(Object user) {
        if (user instanceof com.aihoo.domain.doctor.model.entity.DoctorUser) {
            setLoginUser((com.aihoo.domain.doctor.model.entity.DoctorUser) user);
            return;
        }
        if (user instanceof LoginUser) {
            com.aihoo.security.AuthUtil.setLoginUser((LoginUser) user);
            return;
        }
        // 兜底：忽略
    }

    public static Integer getLoginUserId() {
        String id = com.aihoo.security.AuthUtil.getLoginUserId();
        return id == null ? null : Integer.valueOf(id);
    }

    public static String getLoginUserIds() {
        Integer id = getLoginUserId();
        return id == null ? null : String.valueOf(id);
    }

    public static com.aihoo.domain.doctor.model.entity.DoctorUser getLoginUser() {
        LoginUser loginUser = com.aihoo.security.AuthUtil.getLoginUser();
        if (loginUser == null) return null;
        com.aihoo.domain.doctor.model.entity.DoctorUser u = new com.aihoo.domain.doctor.model.entity.DoctorUser();
        u.setId(loginUser.getId());
        u.setName(loginUser.getName());
        return u;
    }
}
