package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.dao.UserDao;
import com.apicaller.sosotaxi.entity.User;
import com.apicaller.sosotaxi.service.UserService;
import com.apicaller.sosotaxi.utils.QiNiuUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.Kernel;
import java.io.FileInputStream;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-07-11 10:09:16
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String AVATAR_PATH = "avatar";

    @Resource
    private UserDao userDao;

    @Resource
    private QiNiuUtil qiNiuUtil;
    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public User queryById(long userId) {
        return this.userDao.queryById(userId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer userId) {
        return this.userDao.deleteById(userId) > 0;
    }

    @Override
    public User queryUserByUserName(String userName) {
        return userDao.queryUserByUserName(userName);
    }

    @Override
    public boolean ifExistsByUserName(String userName) {
        if (userDao.queryUserByUserName(userName)==null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean storageAvatar(FileInputStream file, String fileName) {
        String filePath = AVATAR_PATH + "-" + fileName;
        return qiNiuUtil.uploadImg(file, filePath) != null;
    }

    @Override
    public String getRealAvatarPath(String fileName) {
        String filePath = AVATAR_PATH + "-" + fileName;
        return qiNiuUtil.getDownloadUrl(filePath);
    }


}