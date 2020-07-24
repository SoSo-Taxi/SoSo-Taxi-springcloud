package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.Order;

import java.util.List;

/**
 * @author 骆荟州
 * @createTime  2020-07-14 23:18:52
 */
public interface OrderService {

    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 实例对象
     */
    Order queryById(Long orderId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Order> queryAllByLimit(int offset, int limit);

    /**
     * 查询指定Id用户的订单
     *
     * @param passengerId 用户Id
     * @return 订单列表
     */
    List<Order> queryByPassengerId(long passengerId);

    /**
     * 查询指定Id司机的订单
     *
     * @param driverId 司机Id
     * @return 订单列表
     */
    List<Order> queryByDriverId(long driverId);

    /**
     * 新增数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    Order insert(Order order);

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 实例对象
     */
    int update(Order order);

    /**
     * 通过主键删除数据
     *
     * @param orderId 主键
     * @return 是否成功
     */
    boolean deleteById(Long orderId);

    /**
     * 获取司机平均评分
     * @param driverId
     * @return
     */
    Double getDriverAvgRate(long driverId);

}