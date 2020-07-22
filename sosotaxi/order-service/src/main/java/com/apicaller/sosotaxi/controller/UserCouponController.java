package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.UserCoupon;
import com.apicaller.sosotaxi.service.UserCouponService;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 骆荟州
 * @updateTime 2020-07-17 11:18:06
 */
@RestController
@RequestMapping("/coupon")
public class UserCouponController {

    @Resource
    private UserCouponService userCouponService;

    @GetMapping("/getById")
    public UserCoupon getById(Long id) {
        return this.userCouponService.queryById(id);
    }

    @GetMapping("/getByUserId")
    public List<UserCoupon> getByUserId(long userId) {
        return userCouponService.queryByUserId(userId);
    }

    @PostMapping("/addCoupon")
    public UserCoupon addCoupon(@RequestBody UserCoupon userCoupon) {
        return userCouponService.insert(userCoupon);
    }

    @DeleteMapping("/deleteCoupon")
    public boolean deleteCoupon(long id) {
        return userCouponService.deleteById(id);
    }
}