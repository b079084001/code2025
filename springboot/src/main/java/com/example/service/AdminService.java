package com.example.service;

import com.example.exception.CustomerException;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    public String admin(String name) {
        if ("admin".equals(name)) {
            return "admin";
        } else {
            throw new CustomerException("账号错误");
        }
    }
}
