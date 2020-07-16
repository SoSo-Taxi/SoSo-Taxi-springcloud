package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/16 11:39 上午
 * @updateTime:
 */
@FeignClient(name = "order-service")
@Service
public interface OrderFeignClient {

    /**
     * 获取用户所有订单
     * @param userId
     * @return 订单列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/getByPassengerId")
    List<Order> getPassengerOrders(@RequestParam("passengerId") long userId);

    /**
     * 获取司机所有订单
     * @param userId
     * @return 订单列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/getByDriverId")
    List<Order> getDriverOrders(@RequestParam("driverId") long userId);
}
