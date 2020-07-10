package com.apicaller.sosotaxi.controller;


import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class TestController {



    @GetMapping(value ="/get")
    public String    getString() {
        return "测试成功";
    }


}

