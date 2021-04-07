package com.example.controller.salary;

import com.example.entity.Salary;
import com.example.entity.help.RespBean;
import com.example.service.salary.SalaryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    SalaryService service;

    public SalaryController(SalaryService service) {
        this.service = service;
    }

    @GetMapping
    public RespBean getAll() {
        return RespBean.ok("工资账套查询成功", service.getAll());
    }

    @PostMapping
    public RespBean add(@RequestBody Salary salary) {
        if (service.add(salary) == 1) return RespBean.ok("工资账套添加成功", 1);
        return RespBean.error("工资账套添加失败");
    }

    @PutMapping
    public RespBean update(@RequestBody Salary salary) {
        if (service.update(salary) == 1) return RespBean.ok("工资账套修改成功", 1);
        return RespBean.error("工资账套修改失败");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id) {
        if (service.delete(id) == 1) return RespBean.ok("工资账套删除成功", 1);
        return RespBean.error("工资账套删除失败");
    }
    @DeleteMapping
    public RespBean deleteMulti(Integer[] ids) {
        if (service.deleteMulti(ids) >= 1) return RespBean.ok("工资账套批量删除成功", 1);
        return RespBean.error("工资账套批量删除失败");
    }
}
