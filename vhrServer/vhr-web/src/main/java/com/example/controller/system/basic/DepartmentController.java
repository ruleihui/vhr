package com.example.controller.system.basic;

import com.example.entity.Department;
import com.example.entity.help.RespBean;
import com.example.service.system.basic.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {
    DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public RespBean getAll() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(service.getAll());
        return RespBean.ok("部门初始化成功", objects);
    }

    @PostMapping
    public RespBean addDepartment(@RequestBody Department department) {
        service.addDepartment(department);
        if (department.getAffectRow() == 1) return RespBean.ok("部门添加成功", department);
        return RespBean.error("部门添加失败");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteDepartment(@PathVariable("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        service.deleteDepartment(map);
        int result = (Integer) map.get("result");
        switch (result) {
            case -2:
                return RespBean.error("删除失败,存在子部门");
            case -1:
                return RespBean.error("删除失败,部门存在员工");
            case 1:
                return RespBean.ok("部门删除成功", 1);
            default:
                return RespBean.error("删除失败");
        }
    }
}
