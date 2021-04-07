package com.example.controller;

import com.example.entity.Hr;
import com.example.entity.help.RespBean;
import com.example.service.HrService;
import com.example.utils.Utils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/hrInfo")
public class HrInfoController {
    HrService hrService;
    PasswordEncoder encoder;

    public HrInfoController(HrService hrService, PasswordEncoder encoder) {
        this.hrService = hrService;
        this.encoder = encoder;
    }

    @GetMapping
    public RespBean get(Authentication auth) {
        return RespBean.ok("获取用户信息成功", auth);
    }

    @PutMapping
    public RespBean update(@RequestBody Hr hr, Authentication auth) {
        if (hrService.updateInfo(hr) == 1) {
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(hr, auth.getCredentials(), auth.getAuthorities()));
            return RespBean.ok("更新用户信息成功", hr);
        }
        return RespBean.error("更新用户信息失败");
    }

    @PutMapping("/password")
    public RespBean updatePassword(@RequestBody HashMap<String, String> values) {
        Hr hr = (Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String old = values.get("old");
        String password = values.get("password");
        String password2 = values.get("password2");
        if (!hrService.matchOld(old, hr.getUsername(), encoder))return RespBean.error("旧密码填写错误");
        if (!Utils.isNull(password2) && !Utils.isNull(password) && password.equals(password2)) {
            password = encoder.encode(password);
            hr.setPassword(password);
            if (hrService.updateInfo(hr) == 1) {
                SecurityContextHolder.clearContext();
                return RespBean.ok("更新密码成功,请重新登录", hr);
            }
        }
        return RespBean.error("更新密码失败");
    }
}
