package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.UserCoupon;
import java.util.List;

/**
 * UserCoupon表服务接口
 *
 * @author 骆荟州
 * @createTime 2020-07-17 11:18:06
 */
public interface UserCouponService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserCoupon queryById(Long id);

    /**
     * 查询一个用户所有的优惠券
     *
     * @param userId
     * @return 实例对象
     */
    List<UserCoupon> queryByUserId(long userId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserCoupon> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param userCoupon 实例对象
     * @return 实例对象
     */
    UserCoupon insert(UserCoupon userCoupon);

    /**
     * 修改数据
     *
     * @param userCoupon 实例对象
     * @return 实例对象
     */
    UserCoupon update(UserCoupon userCoupon);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}