package com.apicaller.sosotaxi.dao;

import com.apicaller.sosotaxi.entity.UserWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * UserWallet表数据库访问层
 *
 * @author 骆荟州
 * @createTime 2020-07-17 10:39:47
 */
@Mapper
public interface UserWalletDao {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    UserWallet queryById(Long userId);

    /**
     * 通过用户名查询单条数据
     *
     * @param username
     * @return 实例对象
     */
    UserWallet queryByUsername(String username);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserWallet> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 新增数据
     *
     * @param userWallet 实例对象
     * @return 影响行数
     */
    int insert(UserWallet userWallet);

    /**
     * 修改数据
     *
     * @param userWallet 实例对象
     * @return 影响行数
     */
    int update(UserWallet userWallet);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 影响行数
     */
    int deleteById(Long userId);

}