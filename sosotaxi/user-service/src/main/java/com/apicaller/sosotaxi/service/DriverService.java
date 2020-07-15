package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;

import java.util.List;

/**
 * (Driver)表服务接口
 *
 * @author 骆荟州
 * @createTime  2020-07-14 21:53:57
 */
public interface DriverService {

    /**
     * 通过ID查询单条数据
     *
     * @param driverId 主键
     * @return 实例对象
     */
    Driver queryById(Long driverId);

    /**
     * 通过用户名查询单条数据
     *
     * @param username
     * @return 实例对象
     */
    Driver queryByUsername(String username);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Driver> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param driver 实例对象
     * @return 实例对象
     */
    int insert(DriverVo driver);

    /**
     * 修改数据
     *
     * @param driver 实例对象
     * @return 实例对象
     */
    int update(DriverVo driver);

    /**
     * 通过主键删除数据
     *
     * @param driverId 主键
     * @return 是否成功
     */
    boolean deleteById(Long driverId);

}