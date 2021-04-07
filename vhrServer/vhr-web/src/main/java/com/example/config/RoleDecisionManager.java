package com.example.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 根据RoleFilter返回的角色,决定是否继续访问
 */
@Component
public class RoleDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //如果是登录请求直接放过,或者在securityConfig,通过WebSecurity配置ignore的请求地址
        //不然每次AccessDenied会再请求login,导致redirect循环逸出
        if (((FilterInvocation) object).getRequestUrl().equals("/login")) return;
        //如果没有登录,需要先登录
        if (authentication instanceof AnonymousAuthenticationToken) throw new AccessDeniedException("请登录");
        //如果已经登录且是ROLE_login角色,满足条件结束判断
        else if (configAttributes.stream().anyMatch(it -> it.getAttribute().equals("ROLE_login"))) return;
        //通过已有的权限匹配需要的权限
        //如果已经登录且持有的角色匹配上任意一个需要的角色,满足条件结束判断
        else if (authentication.getAuthorities().stream().anyMatch(
                it -> configAttributes.stream().map(ConfigAttribute::getAttribute)
                        .anyMatch(it.getAuthority()::equals)
        )) return;
        //如果以上条件都不满足,提示权限不足
       else throw new AccessDeniedException("权限不足,请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
