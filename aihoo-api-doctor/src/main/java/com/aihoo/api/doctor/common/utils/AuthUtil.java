package com.aihoo.api.doctor.common.utils;


import com.aihoo.util.StringUtil;
import com.aihoo.api.doctor.app.model.DoctorUser;

import java.util.HashMap;


public class AuthUtil {
    private static String USERID;

    private static final ThreadLocal<HashMap<String, Object>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    private static final String LOGIN_KEY = "loginUser";

    public static void clear() {
        threadLocal.remove();
    }

    public static void setLoginUser(Object user) {
        threadLocal.get().put(LOGIN_KEY, user);
    }

    /**
     * 获取当前登录的userId
     */
    public static Integer getLoginUserId() {
        DoctorUser loginUser = getLoginUser();
        return loginUser == null ? null : Integer.valueOf(loginUser.getId());
    }

    public static String getLoginUserIds() {
        return StringUtil.getIntegerStr(getLoginUserId());
    }

    /**
     * 获取用户信息
     */
    public static DoctorUser getLoginUser() {
        Object value = threadLocal.get().get(LOGIN_KEY);
        return value == null ? null : (DoctorUser) value;
    }


}
