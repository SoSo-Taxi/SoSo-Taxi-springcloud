package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.entity.UserWallet;
import com.apicaller.sosotaxi.dao.UserWalletDao;
import com.apicaller.sosotaxi.service.UserWalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (UserWallet)表服务实现类
 *
 * @author 骆荟州
 * @since 2020-07-17 10:39:47
 */
@Service("userWalletService")
public class UserWalletServiceImpl implements UserWalletService {
    @Resource
    private UserWalletDao userWalletDao;

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public UserWallet queryById(Long userId) {
        return this.userWalletDao.queryById(userId);
    }

    /**
     * 通过用户名查询单条数据
     *
     * @param username
     * @return 实例对象
     */
    @Override
    public UserWallet queryByUsername(String username) {
        return this.userWalletDao.queryByUsername(username);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<UserWallet> queryAllByLimit(int offset, int limit) {
        return this.userWalletDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userWallet 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(UserWallet userWallet) {
        return this.userWalletDao.insert(userWallet);
    }

    /**
     * 修改数据
     *
     * @param userWallet 实例对象
     * @return 实例对象
     */
    @Override
    public UserWallet update(UserWallet userWallet) {
        this.userWalletDao.update(userWallet);
        return this.queryById(userWallet.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.userWalletDao.deleteById(userId) > 0;
    }
}