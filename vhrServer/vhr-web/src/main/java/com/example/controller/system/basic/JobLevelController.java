package com.example.controller.system.basic;

import com.example.entity.JobLevel;
import com.example.entity.help.RespBean;
import com.example.service.system.basic.JobLevelService;
import com.example.utils.Utils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/basic/jobLevel")
public class JobLevelController {
    JobLevelService service;

    public JobLevelController(JobLevelService service) {
        this.service = service;
    }

    @GetMapping
    public RespBean get() {
        return RespBean.ok("初始化jobLevels数据", service.getAll());
    }

    @PostMapping
    public RespBean add(@RequestBody JobLevel jobLevel) {
        if (isInvalid(jobLevel)) return RespBean.error("添加失败");
        if (service.addJobLevel(jobLevel) == 1) return RespBean.ok("添加成功", 1);
        return RespBean.error("添加失败");
    }

    @PutMapping
    public RespBean update(@RequestBody JobLevel jobLevel) {
        if (isInvalid(jobLevel)) return RespBean.error("添加失败");
        if (service.updateById(jobLevel) == 1) return RespBean.ok("修改成功", 1);
        return RespBean.error("修改失败");
    }

    public boolean isInvalid(JobLevel jobLevel) {
        return Utils.isNull(jobLevel.getName()) || !jobLevel.titleLevelValid(jobLevel.getTitleLevel());
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id) {
        if (service.deleteById(id) == 1) return RespBean.ok("删除成功", 1);
        return RespBean.error("删除失败");
    }

    @DeleteMapping
    public RespBean deleteMulti(Integer[] ids) {
        int num;
        if ((num = service.deleteByIds(ids)) == ids.length) return RespBean.ok("删除成功", num);
        return RespBean.error("删除失败");
    }
}
