package com.aihoo.api.admin.config.security;

import com.aihoo.domain.sys.model.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security Utilities replacing ShiroUtils.
 */
public class SecurityUtils {

    /**
     * Get the current login user.
     */
    public static SysUser getLoginUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
                return ((LoginUser) authentication.getPrincipal()).getSysUser();
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * Get the current login user ID.
     */
    public static Long getLoginUserId() {
        SysUser user = getLoginUser();
        return user != null ? Long.valueOf(user.getId()) : null;
    }

    /**
     * Encrypt password using default MD5 settings.
     */
    public static String encryptPassword(String password) {
        return new Md5PasswordEncoder().encode(password);
    }
}
