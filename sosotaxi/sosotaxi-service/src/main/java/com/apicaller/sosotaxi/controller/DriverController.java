package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.feignClients.DriverInfofeignClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/15 9:33 上午
 * @updateTime:
 */
@RestController
@RequestMapping("/driver")
public class DriverController {
    @Resource
    private DriverInfofeignClient driverInfofeignClient;

    @GetMapping("/getByName")
    public ResponseBean getByName(String userName) {
        Driver driver = driverInfofeignClient.getDriverByName(userName);
        if(driver == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", driver);
    }

    @GetMapping("/getById")
    public ResponseBean getById(long userId) {

        Driver driver = driverInfofeignClient.getDriverById(userId);
        if(driver == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", driver);
    }

    @PostMapping("/addDriver")
    public ResponseBean addDriver(@RequestBody DriverVo driver) {
        int result = driverInfofeignClient.addDriver(driver);
        if(result == 0) {
            return new ResponseBean(201, "添加司机信息失败", null);
        }
        return new ResponseBean(200, "添加司机信息成功", null);
    }

    @PutMapping("/updateDriver")
    public ResponseBean updateDriver(@RequestBody DriverVo driver) {
        int result = driverInfofeignClient.updateDriver(driver);
        if(result == 0) {
            return new ResponseBean(201, "修改司机信息失败", null);
        }
        return new ResponseBean(200, "修改司机信息成功", null);
    }
}
