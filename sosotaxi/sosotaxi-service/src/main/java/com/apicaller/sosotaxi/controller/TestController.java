package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.service.UserService;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    UserService userService;

    @GetMapping(value = "/get")
    public String    getString() {
        return "测试成功";
    }

    @Test
    public void myTest(){
        System.out.println(userService.deleteById(1));
    }
}

