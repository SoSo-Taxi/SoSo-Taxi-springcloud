package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.entity.Order;
import com.apicaller.sosotaxi.dao.OrderDao;
import com.apicaller.sosotaxi.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 骆荟州
 * @createTime  2020-07-14 23:18:54
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;


    @Override
    public Order queryById(Long orderId) {
        return this.orderDao.queryById(orderId);
    }


    @Override
    public List<Order> queryAllByLimit(int offset, int limit) {
        return this.orderDao.queryAllByLimit(offset, limit);
    }


    @Override
    public List<Order> queryByPassengerId(long passengerId) {
        return this.orderDao.queryByPassengerId(passengerId);
    }


    @Override
    public List<Order> queryByDriverId(long driverId) {
        return this.orderDao.queryByDriverId(driverId);
    }


    @Override
    public Order insert(Order order) {
        this.orderDao.insert(order);
        return order;
    }


    @Override
    public int update(Order order) {
        return this.orderDao.update(order);
    }


    @Override
    public boolean deleteById(Long orderId) {
        return this.orderDao.deleteById(orderId) > 0;
    }

    /**
     * 获取司机平均评分
     *
     * @param driverId
     * @return
     */
    @Override
    public Double getDriverAvgRate(long driverId) {
        return orderDao.getDriverAvgRate(driverId);
    }
}