package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.service.PassengerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/14 1:02 下午
 * @updateTime:
 */
@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    private PassengerService passengerService;

    @GetMapping("/test")
    public Passenger getPassenger(String username) {

        return passengerService.queryByUsername(username);
    }
}
