package com.example.controller.system.basic;

import com.example.entity.Position;
import com.example.entity.help.RespBean;
import com.example.service.system.basic.PositionService;
import com.example.utils.Utils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/basic/position")
public class PositionController {
    PositionService service;

    public PositionController(PositionService service) {
        this.service = service;
    }

    @GetMapping
    public RespBean get() {
        return RespBean.ok("初始化positions数据", service.getAll());
    }

    @PostMapping
    public RespBean add(@RequestBody Position position) {
        if (Utils.isNull(position.getName())) return RespBean.error("添加失败");
        if (service.addPosition(position) == 1) return RespBean.ok("添加成功", 1);
        return RespBean.error("添加失败");
    }

    @PutMapping
    public RespBean update(@RequestBody Position position) {
        if (Utils.isNull(position.getId())
                || Utils.isNull(position.getName())) return RespBean.error("修改失败");
        if (service.updateById(position) == 1) return RespBean.ok("修改成功", 1);
        return RespBean.error("修改失败");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id) {
        if (service.deleteById(id) == 1) return RespBean.ok("删除成功", 1);
        return RespBean.error("删除失败");
    }

    @DeleteMapping
    public RespBean deleteMulti(Integer[] ids) {
        if (Utils.isNull(ids)) return RespBean.error("删除失败");
        int num;
        if ((num = service.deleteByIds(ids)) == ids.length) return RespBean.ok("删除成功", num);
        return RespBean.error("删除失败");
    }
}
