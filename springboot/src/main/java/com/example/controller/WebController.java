package com.example.controller;

import com.example.common.Result;
import com.example.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @Resource
    AdminService adminService;

    @GetMapping("/hello")
    public Result hello() {
        return Result.success("Hello World!");
    }

    @GetMapping("/admin")
    public Result admin(String name) {
        String admin = adminService.admin(name);
        return Result.success(admin);
    }

}