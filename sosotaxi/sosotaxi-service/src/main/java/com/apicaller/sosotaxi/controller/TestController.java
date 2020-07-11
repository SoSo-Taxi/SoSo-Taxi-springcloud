package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.entity.Result;
import com.apicaller.sosotaxi.project.entity.User;
import com.apicaller.sosotaxi.service.UserDetailsServiceImpl;
import com.apicaller.sosotaxi.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class TestController {

    @Autowired
    UserService userService;

    @PostMapping(value="/regist")
    public Result regist(User user) {
        return userService.regist(user);
    }
}

