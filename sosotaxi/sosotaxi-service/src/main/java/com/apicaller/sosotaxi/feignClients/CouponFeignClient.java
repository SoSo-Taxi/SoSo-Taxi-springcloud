package com.apicaller.sosotaxi.feignClients;

import com.apicaller.sosotaxi.entity.UserCoupon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 骆荟州
 * @createTime: 2020/7/17 11:46 上午
 * @updateTime:
 */

@FeignClient(name = "order-service")
@Service
public interface CouponFeignClient {
    /**
     * 根据用户Id获取用户所有的优惠券
     * @param userId
     * @return 用户优惠券列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/coupon/getByUserId")
    public List<UserCoupon> getByUserId(@RequestParam("userId") long userId);

    /**
     * 为用户发放优惠券
     * @param userCoupon
     * @return 优惠券信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/coupon/addCoupon")
    public UserCoupon addCoupon(@RequestBody UserCoupon userCoupon);
}
