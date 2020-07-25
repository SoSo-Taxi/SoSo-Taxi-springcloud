package com.apicaller.sosotaxi.dao;

import com.apicaller.sosotaxi.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 *
 * @author 骆荟州
 * @createTime   2020-07-14 23:14:55
 */
@Mapper
public interface OrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param orderId 主键
     * @return 订单
     */
    Order queryById(Long orderId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 订单列表
     */
    List<Order> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

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
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 修改数据
     *
     * @param order 实例对象
     * @return 影响行数
     */
    int update(Order order);

    /**
     * 通过主键删除数据
     *
     * @param orderId 主键
     * @return 影响行数
     */
    int deleteById(Long orderId);

    /**
     * 获取司机平均评分
     *
     * @param driverId
     * @return
     */
    Double getDriverAvgRate(long driverId);

    /**
     * 获取司机订单数量
     *
     * @param driverId
     * @return
     */
    int getDriverOrderNum(long driverId);
}