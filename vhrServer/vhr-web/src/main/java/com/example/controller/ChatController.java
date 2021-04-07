package com.example.controller;

import com.example.entity.help.RespBean;
import com.example.service.HrService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    HrService hrService;

    public ChatController(HrService hrService) {
        this.hrService = hrService;
    }
    @GetMapping("/friends")
    public RespBean getFriends(){
        return RespBean.ok("获取好友成功",hrService.getAllHr(null));
    }
}
