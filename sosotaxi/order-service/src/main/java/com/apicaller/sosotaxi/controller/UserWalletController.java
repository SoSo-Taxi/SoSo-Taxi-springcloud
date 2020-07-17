package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.UserWallet;
import com.apicaller.sosotaxi.service.UserWalletService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/17 10:58 上午
 * @updateTime:
 */
@RestController
@RequestMapping("/wallet")
public class UserWalletController {
    @Resource
    private UserWalletService userWalletService;

    @GetMapping("/getById")
    public UserWallet getById(Long userId) {
        return userWalletService.queryById(userId);
    }

    @GetMapping("/getByUsername")
    public UserWallet getByUsername(String username) {
        return userWalletService.queryByUsername(username);
    }

    @GetMapping("/setupAccount")
    public boolean setupAccount(long userId) {
        return userWalletService.insert(new UserWallet(userId, 0.0, 0)) == 1;
    }

    @PutMapping("/updateAccount")
    public UserWallet updateAccount(@RequestBody UserWallet userWallet) {
        return userWalletService.update(userWallet);
    }
}
