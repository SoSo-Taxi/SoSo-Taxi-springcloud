package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;
import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.entity.PassengerVo;
import com.apicaller.sosotaxi.service.DriverService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 * @author 骆荟州
 * @createTime   2020-07-14 21:53:58
 * @updateTime
 */

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Resource
    private DriverService driverService;

    @GetMapping("/getById")
    public Driver getDriverById(Long userId) {
        return this.driverService.queryById(userId);
    }

    @GetMapping("/getByName")
    public Driver getDriverByName(String username) {
        return driverService.queryByUsername(username);
    }

    @PostMapping("/addDriver")
    public int insertDriver(@RequestBody DriverVo driver) {
        return driverService.insert(driver);
    }

    @PutMapping("/updateDriver")
    public int updateDriver(@RequestBody DriverVo driver) {
        return driverService.update(driver);
    }
}