package com.example.controller.system.basic;

import com.example.entity.Role;
import com.example.entity.help.RespBean;
import com.example.service.MenuService;
import com.example.service.system.basic.RoleService;
import com.example.utils.Utils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/basic/permission")
public class PermissionController {
    RoleService roleService;
    MenuService menuService;

    public PermissionController(RoleService roleService, MenuService menuService) {
        this.roleService = roleService;
        this.menuService = menuService;
    }

    @GetMapping
    public RespBean getAllRoles() {
        return RespBean.ok("获取roles数据", roleService.queryAll(null));
    }

    @GetMapping("/menus")
    public RespBean getAllMenus() {
        return RespBean.ok("获取menus数据", menuService.queryMenuAndChild());
    }

    @GetMapping("/{id}")
    public RespBean getMenusByRoleId(@PathVariable Integer id) {
        return RespBean.ok("根据roleId获取menus数据", menuService.getMenusByRoleId(id));
    }

    @PostMapping("/update/{id}")
    public RespBean updateMenuByRoleId(@PathVariable Integer id,
                                       @RequestBody List<Integer> ids) {
        return RespBean.ok("根据roleId更新对应的menu", menuService.updateMenuByRoleId(id, ids));
    }

    @PostMapping("/add")
    public RespBean add(@RequestBody Role role) {
        if (Utils.isNull(role.getName())
                || !role.getName().startsWith("ROLE_")) return RespBean.error("添加失败");
        if (roleService.add(role) == 1) return RespBean.ok("添加成功", 1);
        return RespBean.error("添加失败");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id) {
        if (roleService.delete(id) >= 1) return RespBean.ok("删除成功", 1);
        return RespBean.error("删除失败");
    }
}
