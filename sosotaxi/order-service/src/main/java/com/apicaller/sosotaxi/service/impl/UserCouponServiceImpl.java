package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.entity.UserCoupon;
import com.apicaller.sosotaxi.dao.UserCouponDao;
import com.apicaller.sosotaxi.service.UserCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserCoupon)表服务实现类
 *
 * @author 骆荟州
 * @since 2020-07-17 11:18:06
 */
@Service("userCouponService")
public class UserCouponServiceImpl implements UserCouponService {
    @Resource
    private UserCouponDao userCouponDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserCoupon queryById(Long id) {
        return this.userCouponDao.queryById(id);
    }

    /**
     * 查询一个用户所有的优惠券
     *
     * @param userId
     * @return 实例对象
     */
    @Override
    public List<UserCoupon> queryByUserId(long userId) {
        return userCouponDao.queryByUserId(userId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<UserCoupon> queryAllByLimit(int offset, int limit) {
        return this.userCouponDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userCoupon 实例对象
     * @return 实例对象
     */
    @Override
    public UserCoupon insert(UserCoupon userCoupon) {
        this.userCouponDao.insert(userCoupon);
        return userCoupon;
    }

    /**
     * 修改数据
     *
     * @param userCoupon 实例对象
     * @return 实例对象
     */
    @Override
    public UserCoupon update(UserCoupon userCoupon) {
        this.userCouponDao.update(userCoupon);
        return this.queryById(userCoupon.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.userCouponDao.deleteById(id) > 0;
    }
}