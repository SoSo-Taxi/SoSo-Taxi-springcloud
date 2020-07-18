package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dispatch/driver")
public class DriverDispatchController {

    @Resource
    InfoCacheServiceImpl infoCacheService;

    /**
     * 将用户信息注册登录到分配服务
     * @param msg
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseBean login(@RequestBody DriverLoginMsg msg) {
        if(msg == null){
            ResponseBean response = new ResponseBean(400, "参数错误", null);
            return response;
        }
        MinimizedDriver driver = msg.getDriver();
        String userId = driver.getDriverId();
        GeoPoint point = msg.getPoint();
        ResponseBean response = new ResponseBean(500, userId + "的请求未被处理", null);
        try{
            response.setCode(200);
            infoCacheService.updateDriverField(driver, point);
            response.setMsg("登录信息成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 为用户更新派单参考的位置信息
     * @param msg
     * @return
     */
    @PostMapping(value = "/update")
    public ResponseBean accept(@RequestBody DriverUpdateMsg msg) {
        if(msg == null){
            ResponseBean response = new ResponseBean(400, "参数错误", null);
            return response;
        }
        String userId = msg.getDriverId();
        GeoPoint point = msg.getPoint();
        ResponseBean response = new ResponseBean(500, userId + "的请求未被处理", null);
        try{
            response.setCode(200);
            MinimizedDriver driver = infoCacheService.getDriver(userId);
            infoCacheService.updateDriverField(driver, point);
            response.setMsg("更新信息成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 为用户接受订单
     * @param msg
     * @return
     */
    @PostMapping(value = "/accept")
    public ResponseBean accept(@RequestBody DriverAcceptMsg msg) {
        if(msg == null){
            ResponseBean response = new ResponseBean(400, "参数错误", null);
            return response;
        }
        String userId = msg.getDriverId();
        ResponseBean response = new ResponseBean(500, userId + "的请求未被处理", null);
        try{
            response.setCode(200);
            String orderId = msg.getOrderId();
            String driverId = msg.getDriverId();

            //TODO:接单成功后删除该司机
            MinimizedDriver driver = infoCacheService.getDriver(driverId);

            if(!infoCacheService.hasDriver(driverId)){
                response.setMsg("登记名单中未找到该司机");
                return response;
            }
            if(!infoCacheService.hasUOrder(orderId)){
                response.setMsg("订单不存在");
                return response;
            }

            Map<String, Object> result = infoCacheService.acceptOrder(orderId);
            Boolean isSuccess = null;
            isSuccess = (Boolean) result.get("isSuccess");
            if(isSuccess == null){
                response.setCode(500);
                response.setMsg("内部服务器错误");
            }
            else if(isSuccess){
                UnsettledOrder order = (UnsettledOrder) result.get("order");
                response.setMsg("接单成功");
                response.setData(order);
            }
            else if(!isSuccess){
                response.setMsg("订单已不存在");
                response.setData(null);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 为用户退出该分配服务
     * @param msg
     * @return
     */
    @PostMapping(value = "/logout")
    public ResponseBean logout(@RequestBody DriverLogoutMsg msg) {
        if(msg == null){
            ResponseBean response = new ResponseBean(400, "参数错误", null);
            return response;
        }
        String userId = msg.getDriverId();
        ResponseBean response = new ResponseBean(500, userId + "的请求未被处理", null);
        try{
            infoCacheService.deleteDriverField(userId);
            response.setMsg("该用户现在不会再收到订单");
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
