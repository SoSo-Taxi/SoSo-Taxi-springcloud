package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.User;

import java.io.FileInputStream;
import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @createTime 2020-07-11 10:09:16
 * @updateTime 2020-07-20 22:13:10
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    User queryById(long userId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer userId);


    /**
     * 通过用户名查询
     * @param userName
     * @return User
     */
    User queryUserByUserName(String userName);


    /**
     * 用户是否存在
     */
    boolean ifExistsByUserName(String userName);

    /**
     * 存储用户头像
     * @param fileName
     * @return 是否成功
     */
    boolean storageAvatar(FileInputStream file, String fileName);

    /**
     * 获取用户头像下载Url
     * @param fileName
     * @return 是否成功
     */
    String getRealAvatarPath(String fileName);
}