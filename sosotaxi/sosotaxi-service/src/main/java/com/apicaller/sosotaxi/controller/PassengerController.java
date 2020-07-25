package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.feignClients.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


/**
 * @author: 骆荟州
 * @createTime: 2020/7/14 3:38 下午
 * @updateTime: 2020/7/16 12:04 下午
 */

@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private UserServiceFeignClient userServiceFeignClient;

    @Resource
    private PassengerFeignClient passengerFeignClient;

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private WalletFeignClient walletFeignClient;

    @Resource
    private CouponFeignClient couponFeignClient;

    @GetMapping("/getByName")
    public ResponseBean getByName(String userName) {
        Passenger passenger = passengerFeignClient.getPassengerByName(userName);
        if(passenger == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", passenger);
    }

    @GetMapping("/getById")
    public ResponseBean getById(long userId) {

        Passenger passenger = passengerFeignClient.getPassengerById(userId);
        if(passenger == null) {
            return new ResponseBean(404,"未找到该乘客的信息", null);
        }
        return new ResponseBean(200,"查询成功", passenger);
    }

    @PostMapping("/addPassenger")
    public ResponseBean addPassenger(@RequestBody PassengerVo passenger) {
        int result = passengerFeignClient.addPassenger(passenger);
        if(result == 0) {
            return new ResponseBean(201, "添加乘客个人信息失败", null);
        }
        return new ResponseBean(200, "添加乘客个人信息成功", null);
    }

    @PutMapping("/updatePassenger")
    public ResponseBean updatePassenger(@RequestBody Passenger passenger) {

        User user = new User();
        user.setUserId(passenger.getUserId());
        user.setPhoneNumber(passenger.getPhoneNumber());
        user.setBirthYear(passenger.getBirthYear());
        user.setGender(passenger.getGender());
        user.setIdCardNumber(passenger.getGender());
        user.setRealName(passenger.getRealName());

        userServiceFeignClient.updateUser(user);
        PassengerVo passengerVo = PassengerVo.fromPassenger(passenger);

        int result = passengerFeignClient.updatePassenger(passengerVo);
        if(result == 0) {
            return new ResponseBean(201, "修改乘客个人信息失败", null);
        }
        return new ResponseBean(200, "修改乘客个人信息成功", null);
    }

    @GetMapping("/getOrders")
    public ResponseBean getOrders(long userId) {
        List<Order> orders = orderFeignClient.getPassengerOrders(userId);
        if(orders == null || orders.isEmpty()) {
            return new ResponseBean(404, "未找到该乘客的任何订单", orders);
        }
        return new ResponseBean(200, "找到该乘客的订单记录", orders);
    }

    @GetMapping("/rateForDriver")
    public ResponseBean rateForDriver(long orderId, double rate) {
        boolean result = orderFeignClient.rateForDriver(orderId, rate);
        if(result) {
            return new ResponseBean(200, "评价司机成功", null);
        }
        return new ResponseBean(403, "评价司机失败", null);
    }

    @GetMapping("/payOrder")
    public ResponseBean payOrder(long userId, long orderId, int pointDiscount, Long couponId) {
        UserWallet wallet = walletFeignClient.getById(userId);
        if(wallet == null) {
            return new ResponseBean(403, "未找到该乘客的账户", null);
        }
        Order order = orderFeignClient.getOrder(orderId);
        if(order == null) {
            return new ResponseBean(403, "未找到该订单", null);
        }

        double realCost = 0;
        if(couponId != null) {
            UserCoupon coupon = couponFeignClient.getById(couponId);
            if(coupon == null || coupon.getUserId() != userId) {
                return new ResponseBean(403, "未找到该优惠券", null);
            }
            realCost = order.getCost() - (double)pointDiscount / 100 - coupon.getWorth();
            order.setCouponDiscount(coupon.getWorth());
        }
        else {
            realCost = order.getCost() - (double)pointDiscount / 100;
            order.setCouponDiscount(0.0);
        }

        if(wallet.getBalance() < realCost || wallet.getPoint() < pointDiscount) {
            return new ResponseBean(403, "账户余额不足", null);
        }
        order.setPointDiscount((double)pointDiscount / 100);
        order.setStatus(5);
        wallet.setBalance(wallet.getBalance() - realCost);
        wallet.setPoint(wallet.getPoint() - pointDiscount);

        orderFeignClient.updateOrder(order);
        walletFeignClient.updateAccount(wallet);
        couponFeignClient.deleteCoupon(couponId);
        return new ResponseBean(200, "支付成功", null);
    }

    @GetMapping("/getWalletById")
    public ResponseBean getWalletById(long userId) {
        UserWallet userWallet = walletFeignClient.getById(userId);
        if(userWallet == null) {
            return new ResponseBean(403, "未找到该用户的钱包，请重试", null);
        }
        return new ResponseBean(200, "找到该用户的钱包信息", userWallet);
    }

    @GetMapping("/getCouponById")
    public ResponseBean getCouponById(long userId) {
        List<UserCoupon> coupons = couponFeignClient.getByUserId(userId);
        if(coupons == null || coupons.isEmpty()) {
            return new ResponseBean(403, "该用户没有优惠券", null);
        }
        return new ResponseBean(200, "找到该用户的优惠券", coupons);
    }
}
