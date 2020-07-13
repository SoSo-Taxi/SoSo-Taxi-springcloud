package com.apicaller.sosotaxi.dao;

import com.apicaller.sosotaxi.entity.Passenger;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * Passenger表数据库访问层
 *
 * @author 骆荟州
 * @createTime  2020-07-13 21:56:48
 * @updateTime
 */
@Mapper
public interface PassengerDao {

    /**
     * 通过ID查询单条数据
     *
     * @param passengerId 主键
     * @return 实例对象
     */
    Passenger queryById(Long passengerId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Passenger> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param passenger 实例对象
     * @return 对象列表
     */
    List<Passenger> queryAll(Passenger passenger);

    /**
     * 新增数据
     *
     * @param passenger 实例对象
     * @return 影响行数
     */
    int insert(Passenger passenger);

    /**
     * 修改数据
     *
     * @param passenger 实例对象
     * @return 影响行数
     */
    int update(Passenger passenger);

    /**
     * 通过主键删除数据
     *
     * @param passengerId 主键
     * @return 影响行数
     */
    int deleteById(Long passengerId);

}