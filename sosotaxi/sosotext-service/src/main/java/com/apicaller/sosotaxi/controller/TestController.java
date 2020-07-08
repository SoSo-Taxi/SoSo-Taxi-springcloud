package com.apicaller.sosotaxi.controller;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/get")
    public String getString() {
        return "测试成功";
    }

}

