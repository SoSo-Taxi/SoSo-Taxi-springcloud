package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/16 11:39 上午
 * @updateTime: 2020/7/23 3:40 下午
 */
@FeignClient(name = "order-service")
@Service
public interface OrderFeignClient {

    /**
     * 根据Id获取订单。
     * @param orderId
     * @return 订单
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/getById")
    Order getOrder(@RequestParam("orderId") long orderId);


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


    /**
     * 增加订单。
     * 必须非空的字段为city, departPoint, destPoint, serviceType, createTime
     * @param order
     * @return 新增的order，其id字段将变成数据库中的自增主键。
     */
    @RequestMapping(method = RequestMethod.POST, value = "/order/addOrder")
    Order addOrder(@RequestBody Order order);

    /**
     * 更新订单信息。
     * @param order
     * @return 更新是否成功。
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/order/updateOrder")
    boolean updateOrder(@RequestBody Order order);

    /**
     * 删除订单。
     * @param orderId
     * @return 删除是否成功。
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/order/deleteOrder")
    boolean deleteOrder(@RequestParam("orderId") long orderId);

    /**
     * 为司机评分。
     * @param orderId
     * @param rate
     * @return 评分是否成功。
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/rateForDriver")
    boolean rateForDriver(@RequestParam("orderId") long orderId, @RequestParam("rate") double rate);

    /**
     * 为乘客评分。
     * @param orderId
     * @param rate
     * @return 评分是否成功。
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/rateForPassenger")
    boolean rateForPassenger(@RequestParam("orderId") long orderId, @RequestParam("rate") double rate);

    /**
     * 获取司机平均评分。
     * @param driverId
     * @return avgRate
     */
    @RequestMapping(method = RequestMethod.GET, value = "/order/getDriverAvgRate")
    Double getDriverAvgRate(@RequestParam("driverId") long driverId);
}
