package com.example.config;

import com.example.entity.Menu;
import com.example.entity.Role;
import com.example.service.MenuService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 每次请求获取角色
 * 根据用户传递的url,分析出请求需要的角色
 */
@Component
public class RoleFilter implements FilterInvocationSecurityMetadataSource {
    MenuService menuService;
    AntPathMatcher matcher = new AntPathMatcher();

    public RoleFilter(MenuService menuService) {
        this.menuService = menuService;
    }

    //are designed to perform lookups keyed on FilterInvocations.
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //每次请求都会调用,最好添加一个缓存
        List<Menu> allMenusWithRole= menuService.getAllMenusWithRole();
        //通过过滤器获取调用的请求
        FilterInvocation invocation = ((FilterInvocation) object);
        String url = invocation.getRequestUrl();
        //返回当前请求路径可以匹配的角色
        List<ConfigAttribute> list = allMenusWithRole.stream()
                .filter(it -> it.getUrl() != null && matcher.match(it.getUrl(), url))
                .map(Menu::getRoles)
                .flatMap(List::stream)
                .map(it -> new SecurityConfig(it.getName()))
                .collect(Collectors.toList());
        //如果当前路径没有对应角色需要满足,则至少需要已登录
        return list.size() == 0 ? SecurityConfig.createList("ROLE_login") : list;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    //表示是否支持这种方式
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
