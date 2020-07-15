package com.apicaller.sosotaxi.dao;

import com.apicaller.sosotaxi.entity.Driver;
import com.apicaller.sosotaxi.entity.DriverVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * driver表数据库访问层
 *
 * @author 骆荟州
 * @createTime  2020-07-14 21:53:57
 * @updateTime
 */
@Mapper
public interface DriverDao {

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
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Driver> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);



    /**
     * 新增数据
     *
     * @param driver 实例对象
     * @return 影响行数
     */
    int insert(DriverVo driver);

    /**
     * 修改数据
     *
     * @param driver 实例对象
     * @return 影响行数
     */
    int update(DriverVo driver);

    /**
     * 通过主键删除数据
     *
     * @param driverId 主键
     * @return 影响行数
     */
    int deleteById(Long driverId);

}