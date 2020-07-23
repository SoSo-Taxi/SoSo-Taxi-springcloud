package com.apicaller.sosotaxi.controller;


import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.feignClients.DriverFeignClient;
import com.apicaller.sosotaxi.feignClients.OrderFeignClient;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/15 9:33 上午
 * @updateTime: 2020/7/16 12:04 下午
 */
@RestController
@RequestMapping("/driver")
public class DriverController {
    @Resource
    private DriverFeignClient driverFeignClient;

    @Resource
    private OrderFeignClient orderFeignClient;

    @GetMapping("/getByName")
    public ResponseBean getByName(String userName) {
        Driver driver = driverFeignClient.getDriverByName(userName);
        if(driver == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", driver);
    }

    @GetMapping("/getById")
    public ResponseBean getById(long userId) {

        Driver driver = driverFeignClient.getDriverById(userId);
        if(driver == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", driver);
    }

    @PostMapping("/addDriver")
    public ResponseBean addDriver(@RequestBody DriverVo driver) {
        int result = driverFeignClient.addDriver(driver);
        if(result == 0) {
            return new ResponseBean(201, "添加司机信息失败", null);
        }
        return new ResponseBean(200, "添加司机信息成功", null);
    }

    @PutMapping("/updateDriver")
    public ResponseBean updateDriver(@RequestBody DriverVo driver) {
        int result = driverFeignClient.updateDriver(driver);
        if(result == 0) {
            return new ResponseBean(201, "修改司机信息失败", null);
        }
        return new ResponseBean(200, "修改司机信息成功", null);
    }

    @GetMapping("/getOrders")
    public ResponseBean getOrders(long userId) {
        List<Order> orders = orderFeignClient.getDriverOrders(userId);
        if(orders == null || orders.isEmpty()) {
            return new ResponseBean(404, "未找到该司机的任何订单", orders);
        }
        return new ResponseBean(200, "找到该司机的订单记录", orders);
    }

    @GetMapping("/rateForPassenger")
    public ResponseBean rateForPassenger(long orderId, double rate) {
        boolean result = orderFeignClient.rateForPassenger(orderId, rate);
        if(result) {
            return new ResponseBean(200, "评价乘客成功", null);
        }
        return new ResponseBean(403, "评价乘客失败", null);
    }
}
