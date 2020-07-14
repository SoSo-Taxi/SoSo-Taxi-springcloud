package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.entity.PassengerVo;
import com.apicaller.sosotaxi.service.PassengerService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getByName")
    public Passenger getPassengerByName(String username) {
        return passengerService.queryByUsername(username);
    }

    @GetMapping("/getById")
    public Passenger getPassengerById(long userId) {
        return passengerService.queryById(userId);
    }

    @PostMapping("/addPassenger")
    public int insertPassenger(@RequestBody PassengerVo passenger) {
        return passengerService.insert(passenger);
    }

    @PutMapping("/updatePassenger")
    public int updatePassenger(@RequestBody PassengerVo passenger) {
        return passengerService.update(passenger);
    }

}
