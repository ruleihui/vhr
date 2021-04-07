package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/employee/basic/hello")
    public String basicHello() {
        return "basicHello";
    }

    @GetMapping("/personnel/emp/hello")
    public String empHello() {
        return "empHello";
    }
}
