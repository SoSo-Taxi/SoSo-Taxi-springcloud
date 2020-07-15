package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.dao.DriverDao;
import com.apicaller.sosotaxi.entity.DriverVo;
import com.apicaller.sosotaxi.service.DriverService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * driver表服务实现类
 *
 * @author 骆荟州
 * @createTime  2020-07-14 21:53:57
 */
@Service("driverService")
public class DriverServiceImpl implements DriverService {
    @Resource
    private DriverDao driverDao;

    /**
     * 通过userId(driverId)查询单条数据
     *
     * @param driverId 主键
     * @return 实例对象
     */
    @Override
    public Driver queryById(Long driverId) {
        return this.driverDao.queryById(driverId);
    }

    /**
     * 通过用户名查询单条数据
     *
     * @param username
     * @return 实例对象
     */
    @Override
    public Driver queryByUsername(String username) {
        return this.driverDao.queryByUsername(username);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Driver> queryAllByLimit(int offset, int limit) {
        return this.driverDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增司机
     *
     * @param driver 实例对象
     * @return 影响行数
     */
    @Override
    public int insert(DriverVo driver) {
        return this.driverDao.insert(driver);
    }

    /**
     * 修改司机信息
     *
     * @param driver 实例对象
     * @return 影响行数
     */
    @Override
    public int update(DriverVo driver) {
        return this.driverDao.update(driver);
    }

    /**
     * 通过主键删除数据
     *
     * @param driverId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long driverId) {
        return this.driverDao.deleteById(driverId) > 0;
    }
}