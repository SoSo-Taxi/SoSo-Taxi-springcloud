package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.entity.dispatchservice.MinimizedDriver;
import com.apicaller.sosotaxi.entity.dispatchservice.UnsettledOrder;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverLoginMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverLogoutMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.DriverUpdateMsg;
import com.apicaller.sosotaxi.entity.dispatchservice.message.OpsForOrderMsg;
import com.apicaller.sosotaxi.service.impl.DispatchServiceImpl;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/dispatch/driver")
public class DriverDispatchController {

    @Resource
    InfoCacheServiceImpl infoCacheService;

    @Resource
    DispatchServiceImpl dispatchService;

    @Resource
    PassengerDispatchController passengerDispatchController;

    /**
     * 将用户信息注册登录到分配服务
     * @param msg
     * @return 为真则正常执行，为假说明重复登录，为空则执行错误
     */
    @PostMapping(value = "/login")
    public Boolean login(@RequestBody DriverLoginMsg msg) throws Exception {
        if(msg == null){
            throw new Exception("在登录状态接口中登录时登录参数异常");
        }
        Boolean isSuccess = null;
        MinimizedDriver driver = msg.getDriver();
        String userId = driver.getDriverId();
        GeoPoint point = msg.getPoint();
        try{
            isSuccess = !infoCacheService.getAllDrivers().contains(userId);
            infoCacheService.updateDriverField(driver, point);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("在登录状态接口中更新司机信息时出现错误");
        }
        return isSuccess;
    }

    /**
     * 为用户更新派单参考的位置信息
     * @param msg
     * @return
     */
    @PostMapping(value = "/update")
    public Boolean update(@RequestBody DriverUpdateMsg msg) throws Exception {
        if(msg == null){
            throw new Exception("在更新状态接口中登录时登录参数异常");
        }
        Boolean isSuccess = null;
        String userId = msg.getDriverId();
        GeoPoint point = msg.getPoint();
        try{
            isSuccess = infoCacheService.getAllDrivers().contains(userId);
            if(!isSuccess){
                throw new Exception("该司机并未在接单表中注册过，请使用完整信息注册");
            }
            MinimizedDriver driver = infoCacheService.getDriver(userId);
            infoCacheService.updateDriverField(driver, point);
        }catch (Exception e){
            e.printStackTrace();
//            throw new Exception("未知错误发生在update接口");
        }
        return isSuccess;
    }

    /**
     * 为用户接受订单
     * 返回空时可能是订单已被接受，也有可能是内部服务器错误
     * 为了区分后期可以考虑用Result类包装结果或者增加返回值
     * 如果仅仅是为了演示就这样也行
     * @param msg
     * @return
     */
    @PostMapping(value = "/accept")
    public UnsettledOrder accept(@RequestBody OpsForOrderMsg msg) throws Exception {
        if(msg == null){
            throw new Exception("参数错误");
        }
        String userId = msg.getDriverId();
        //抢单模式处理
        if(dispatchService.getDispatchMethod()== DispatchServiceImpl.DispatchMethod.GROUPDISPATCH){
            UnsettledOrder order = null;
            try{
                String orderId = msg.getOrderId();
                String driverId = msg.getDriverId();

                //接单成功后需要删除该司机
                MinimizedDriver driver = infoCacheService.getDriver(driverId);

                if(!infoCacheService.hasDriver(driverId)){
                    throw new Exception("登记名单中未找到该司机");
                }

                if(!infoCacheService.hasUOrder(orderId)){
                    throw new Exception("订单不存在");
                }

                Map<String, Object> result = infoCacheService.acceptOrder(orderId);
                Boolean isSuccess = null;
                isSuccess = (Boolean) result.get("isSuccess");

                if(isSuccess == null){
                    throw new Exception("接单算法出错");
                }
                else if(isSuccess){
                    order = (UnsettledOrder) result.get("order");

                    //利用提前拿到的司机数据删除该司机的剩余信息
                    //暂时用不到这个方案
                }

                return order;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        else if(dispatchService.getDispatchMethod() == DispatchServiceImpl.DispatchMethod.AUTOARRANGE){
            //删除该订单的已分配过的司机的记录信息
            UnsettledOrder order = infoCacheService.getUOrder(msg.getOrderId());
            try{
                infoCacheService.deleteDispatchedSet(msg.getOrderId());
                infoCacheService.deleteUOrder(msg.getOrderId());
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("已经接受过的订单未被成功删除，订单号："+order.getOrderId());
            }
            return order;
        }
        else{
            throw new Exception("服务器派单策略配置错误");
        }
    }

    /**
     * 拒绝订单
     *
     * 刚刚删掉了responseBean，在重写result之前无法判断错误原因
     * 后面有机会补上，但可能调用方也要改
     * 这样其实也行
     *
     * @param msg
     * @return 重新分配到的司机
     */
    @PostMapping(value = "/refuse")
    public MinimizedDriver refuseOrder(@RequestBody OpsForOrderMsg msg) throws Exception {
        if(msg == null){
            throw new Exception("参数错误");
        }
        String userId = msg.getDriverId();
        MinimizedDriver driver = null;

        //由于传递的信息都是最小化的，在接单消息中缺少必要的登录信息，故司机拒绝单后需要再次调用登录接口

        //司机拒绝后需要再次提交派单申请以将请求转达给其他司机
        UnsettledOrder order = infoCacheService.getUOrder(msg.getOrderId());
        try{
            driver = passengerDispatchController.submit(order);
        }catch (Exception e){
            e.printStackTrace();
        }

        return driver;
    }

    /**
     * 为用户退出该分配服务
     * @param msg
     * @return
     */
    @PostMapping(value = "/logout")
    public Boolean logout(@RequestBody DriverLogoutMsg msg) throws Exception {
        if(msg == null){
            throw new Exception("参数错误");
        }
        Boolean isSuccess = null;
        String userId = msg.getDriverId();
        try{
            isSuccess = infoCacheService.getAllDrivers().contains(userId);
            if(isSuccess){
                infoCacheService.deleteDriverField(userId);
            }
            //"该用户现在不会再收到订单"

        }catch (Exception e){
            e.printStackTrace();
            isSuccess = null;
        }
        return isSuccess;
    }

}
