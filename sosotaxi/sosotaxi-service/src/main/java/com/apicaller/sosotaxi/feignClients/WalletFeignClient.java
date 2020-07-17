package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.UserWallet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/17 11:44 上午
 * @updateTime:
 */

@FeignClient(name = "order-service")
@Service
public interface WalletFeignClient {
    /**
     * 根据Id获取用户钱包信息
     * @param userId
     * @return 用户钱包信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet/getById")
    public UserWallet getById(@RequestParam("userId") long userId);

    /**
     * 根据用户名获取用户钱包信息
     * @param username
     * @return 用户钱包信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet/getByUsername")
    public UserWallet getByUsername(@RequestParam("username") String username);

    /**
     * 设立用户钱包账户
     * @param userId
     * @return 建立账户是否成功
     */
    @RequestMapping(method = RequestMethod.GET, value = "/wallet/setupAccount")
    public boolean setupAccount(@RequestParam("userId") long userId);

    /**
     * 更新用户钱包信息
     * @param userWallet
     * @return 更新后的信息
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/wallet/updateAccount")
    public UserWallet updateAccount(@RequestBody UserWallet userWallet);

}
