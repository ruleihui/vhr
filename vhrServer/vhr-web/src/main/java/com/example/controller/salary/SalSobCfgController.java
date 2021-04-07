package com.example.controller.salary;

import com.example.entity.help.RespBean;
import com.example.service.employee.EmployeeService;
import com.example.service.salary.SalSobCfgService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary/sobCfg")
public class SalSobCfgController {
    EmployeeService service;
    SalSobCfgService sService;


    public SalSobCfgController(EmployeeService service, SalSobCfgService sService) {
        this.sService = sService;
        this.service = service;
    }

    @GetMapping
    public RespBean getEmployeeSalaryByPage(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return RespBean.ok("查询成功", service.getEmployeeSalaryByPage(page, size));
    }

    @PostMapping("/{eid}/{sid}")
    public RespBean add(@PathVariable("eid") Integer employeeId,
                           @PathVariable("sid") Integer salaryId) {
        if (sService.add(employeeId, salaryId) == 1) return RespBean.ok("员工账套添加成功", 1);
        return RespBean.error("员工账套添加失败");
    }
    @PutMapping("/{eid}/{sid}")
    public RespBean update(@PathVariable("eid") Integer employeeId,
                           @PathVariable("sid") Integer salaryId) {
        if (sService.update(employeeId, salaryId) == 1) return RespBean.ok("员工账套修改成功", 1);
        return RespBean.error("员工账套修改失败");
    }
}
