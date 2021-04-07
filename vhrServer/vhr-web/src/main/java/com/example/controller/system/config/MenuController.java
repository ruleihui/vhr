package com.example.controller.system.config;

import com.example.entity.Hr;
import com.example.entity.help.RespBean;
import com.example.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/config")
public class MenuController {
    MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping("/menu")
    public RespBean getMenusByHrId() {
        Hr hr = (Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RespBean bean = RespBean.ok("init menu成功", service.getMenusByHrId(hr.getId()));
        return bean;
    }
}
