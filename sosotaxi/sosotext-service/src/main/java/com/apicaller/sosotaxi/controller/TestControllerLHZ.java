package com.apicaller.sosotaxi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllerLHZ {

    @GetMapping(value = "/lhz")
    public String getString() {
        return "test";
    }

}
