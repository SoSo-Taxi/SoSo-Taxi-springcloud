package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.Passenger;
import java.util.List;


/**
 * Passenger表服务接口
 *
 * @author 骆荟州
 * @createTime  2020-07-13 21:56:48
 * @updateTime
 */
public interface PassengerService {

    /**
     * 通过ID查询单条数据
     *
     * @param passengerId 主键
     * @return 实例对象
     */
    Passenger queryById(Long passengerId);

    /**
     * 通过用户名查询单条数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    Passenger queryByUsername(String username);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Passenger> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param passenger 实例对象
     * @return 实例对象
     */
    Passenger insert(Passenger passenger);

    /**
     * 修改数据
     *
     * @param passenger 实例对象
     * @return 实例对象
     */
    Passenger update(Passenger passenger);

    /**
     * 通过主键删除数据
     *
     * @param passengerId 主键
     * @return 是否成功
     */
    boolean deleteById(Long passengerId);

}