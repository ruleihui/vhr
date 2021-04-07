package com.example.controller.system.hr;

import com.example.entity.Hr;
import com.example.entity.help.RespBean;
import com.example.service.HrService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/system/hr")
public class HrController {
    HrService hrService;

    public HrController(HrService hrService) {
        this.hrService = hrService;
    }

    @GetMapping
    public RespBean getAllHr(String username) {
        return RespBean.ok("hr查询成功", hrService.getAllHr(username));
    }


    @DeleteMapping("/{id}")
    public RespBean deleteById(@PathVariable Integer id) {
        if (hrService.deleteById(id) == 1) return RespBean.ok("hr删除成功", 1);
        return RespBean.error("hr删除失败");
    }

    @PutMapping
    public RespBean isEnable(@RequestBody Hr hr) {
        int num;
        if ((num = hrService.updateEnable(hr)) == 1) return RespBean.ok("修改启用状态成功", 1);
        return RespBean.error("修改启用状态失败");
    }

    @PutMapping("/roles")
    public RespBean roles(@RequestBody Hr hr) {
        int num;
        if ((num = hrService.updateHrRoles(hr)) != 0) return RespBean.ok("修改启用状态成功", 1);
        return RespBean.error("修改启用状态失败");
    }

}
