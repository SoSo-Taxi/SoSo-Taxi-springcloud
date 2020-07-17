package com.apicaller.sosotaxi.dao;

import com.apicaller.sosotaxi.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * UserCoupon表数据库访问层
 *
 * @author 骆荟州
 * @createTime 2020-07-17 11:18:06
 */
@Mapper
public interface UserCouponDao {

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
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserCoupon> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 新增数据
     *
     * @param userCoupon 实例对象
     * @return 影响行数
     */
    int insert(UserCoupon userCoupon);

    /**
     * 修改数据
     *
     * @param userCoupon 实例对象
     * @return 影响行数
     */
    int update(UserCoupon userCoupon);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}