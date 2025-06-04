package com.example.controller;

import com.example.common.Result;
import com.example.service.AdminService1;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController1 {
    @Resource
    AdminService1 adminService1;

    @GetMapping("/hello")
    public Result hello() {
        return Result.success("Hello World!");
    }

    @GetMapping("/admin")
    public Result admin(String name) {
        String admin = adminService1.admin(name);
        return Result.success(admin);
    }

}