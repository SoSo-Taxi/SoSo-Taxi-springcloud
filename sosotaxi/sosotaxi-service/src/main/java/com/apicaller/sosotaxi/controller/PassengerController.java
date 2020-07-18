package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.entity.PassengerVo;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import com.apicaller.sosotaxi.feignClients.PassengerFeignClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


/**
 * @author: 骆荟州
 * @createTime: 2020/7/14 3:38 下午
 * @updateTime: 2020/7/16 12:04 下午
 */

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerFeignClient passengerFeignClient;

    @Resource
    private OrderFeignClient orderFeignClient;

    @GetMapping("/getByName")
    public ResponseBean getByName(String userName) {
        Passenger passenger = passengerFeignClient.getPassengerByName(userName);
        if(passenger == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", passenger);
    }

    @GetMapping("/getById")
    public ResponseBean getById(long userId) {

        Passenger passenger = passengerFeignClient.getPassengerById(userId);
        if(passenger == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", passenger);
    }

    @PostMapping("/addPassenger")
    public ResponseBean addPassenger(@RequestBody PassengerVo passenger) {
        int result = passengerFeignClient.addPassenger(passenger);
        if(result == 0) {
            return new ResponseBean(201, "添加乘客个人信息失败", null);
        }
        return new ResponseBean(200, "添加乘客个人信息成功", null);
    }

    @PutMapping("/updatePassenger")
    public ResponseBean updatePassenger(@RequestBody PassengerVo passenger) {
        int result = passengerFeignClient.updatePassenger(passenger);
        if(result == 0) {
            return new ResponseBean(201, "修改乘客个人信息失败", null);
        }
        return new ResponseBean(200, "修改乘客个人信息成功", null);
    }

    @GetMapping("/getOrders")
    public ResponseBean getOrders(long userId) {
        List<Order> orders = orderFeignClient.getPassengerOrders(userId);
        if(orders == null || orders.isEmpty()) {
            return new ResponseBean(404, "未找到该乘客的任何订单", orders);
        }
        return new ResponseBean(200, "找到该乘客的订单记录", orders);
    }

}
