package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.service.DriverService;
import com.apicaller.sosotaxi.utils.BDmapUtil;
import com.apicaller.sosotaxi.utils.CoordType;
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

        System.out.println("__________________________");
        double distance = BDmapUtil.calcDistance(new GeoPoint(108.385348,22.819243).reverseCoord(),
                new GeoPoint(108.398139,22.818943).reverseCoord(), CoordType.bd09ll);
        System.out.println(distance);
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