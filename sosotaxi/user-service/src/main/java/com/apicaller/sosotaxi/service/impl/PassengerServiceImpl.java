package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.dao.PassengerDao;
import com.apicaller.sosotaxi.dao.UserDao;
import com.apicaller.sosotaxi.entity.Passenger;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.service.PassengerService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Passenger表服务实现类
 *
 * @author 骆荟州
 * @createTime 2020-07-13 21:56:48
 * @updateTime
 */
@Service("passengerService")
public class PassengerServiceImpl implements PassengerService {
    @Resource
    private PassengerDao passengerDao;

    @Resource
    private UserDao userDao;
    /**
     * 通过ID查询单条数据
     *
     * @param passengerId 主键
     * @return 实例对象
     */
    @Override
    public Passenger queryById(Long passengerId) {
        return passengerDao.queryById(passengerId);
    }

    @Override
    public Passenger queryByUsername(String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }
        return passengerDao.queryByUsername(username);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<Passenger> queryAllByLimit(int offset, int limit) {
        return this.passengerDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param passenger 实例对象
     * @return 实例对象
     */
    @Override
    public Passenger insert(Passenger passenger) {
        this.passengerDao.insert(passenger);
        return passenger;
    }

    /**
     * 修改数据
     *
     * @param passenger 实例对象
     * @return 实例对象
     */
    @Override
    public Passenger update(Passenger passenger) {
        this.passengerDao.update(passenger);
        return this.queryById(passenger.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param passengerId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long passengerId) {
        return this.passengerDao.deleteById(passengerId) > 0;
    }
}