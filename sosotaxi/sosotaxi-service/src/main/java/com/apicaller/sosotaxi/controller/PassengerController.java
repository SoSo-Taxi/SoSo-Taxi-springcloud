package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.entity.PassengerVo;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.feignClients.PassengerInfoFeignClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * @author: 骆荟州
 * @createTime: 2020/7/14 3:38 下午
 * @updateTime:
 */

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerInfoFeignClient passengerInfoFeignClient;

    @GetMapping("/getByName")
    public ResponseBean getByName(String userName) {

        Passenger passenger = passengerInfoFeignClient.getPassengerByName(userName);
        if(passenger == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", passenger);
    }

    @GetMapping("/getById")
    public ResponseBean getById(long userId) {

        Passenger passenger = passengerInfoFeignClient.getPassengerById(userId);
        if(passenger == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", passenger);
    }

    @PutMapping("/addPassenger")
    public ResponseBean addPassenger(@RequestBody PassengerVo passenger) {
        int result = passengerInfoFeignClient.addPassenger(passenger);
        if(result == 0) {
            return new ResponseBean(201, "添加乘客个人信息失败", null);
        }
        return new ResponseBean(200, "添加乘客个人信息成功", null);
    }

    @PutMapping("/updatePassenger")
    public ResponseBean updatePassenger(@RequestBody PassengerVo passenger) {
        int result = passengerInfoFeignClient.updatePassenger(passenger);
        if(result == 0) {
            return new ResponseBean(201, "修改乘客个人信息失败", null);
        }
        return new ResponseBean(200, "修改乘客个人信息成功", null);
    }
}
