package com.aihoo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security Utilities — 共享版本，泛型 API 避免对具体实体（SysUser/LoginUser）的硬依赖。
 *
 * <p>原 admin 的 com.aihoo.admin.common.security.SecurityUtils 直接返回 SysUser，
 * 紧耦合 domain-sys。本版本只返回 String userId，调用方按需 cast principal。</p>
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户的 ID（String 类型，匹配各域实体的 String id）。
     * 如果 principal 有 getSysUser().getId()（如 LoginUser），使用之；
     * 否则尝试直接调用 principal.getId()。
     *
     * @return userId，未登录或 principal 不合规返回 null
     */
    public static String getLoginUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return null;
            }
            Object principal = authentication.getPrincipal();
            return extractId(principal);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过反射提取 id：先试 getSysUser().getId()（LoginUser 模式），再试 principal.getId()。
     */
    private static String extractId(Object principal) {
        if (principal == null) {
            return null;
        }
        try {
            Object sysUser = principal.getClass().getMethod("getSysUser").invoke(principal);
            if (sysUser != null) {
                Object id = sysUser.getClass().getMethod("getId").invoke(sysUser);
                return id == null ? null : id.toString();
            }
        } catch (NoSuchMethodException ignore) {
            // principal has no getSysUser; try getId directly
        } catch (Exception e) {
            return null;
        }
        try {
            Object id = principal.getClass().getMethod("getId").invoke(principal);
            return id == null ? null : id.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Encrypt password using default MD5 settings.
     */
    public static String encryptPassword(String password) {
        return new Md5PasswordEncoder().encode(password);
    }
}
