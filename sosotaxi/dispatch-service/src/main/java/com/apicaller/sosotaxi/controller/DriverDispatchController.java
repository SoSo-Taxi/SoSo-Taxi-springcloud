package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.*;
import com.apicaller.sosotaxi.entity.message.DriverLoginMsg;
import com.apicaller.sosotaxi.entity.message.DriverLogoutMsg;
import com.apicaller.sosotaxi.entity.message.DriverUpdateMsg;
import com.apicaller.sosotaxi.entity.message.OpsForOrderMsg;
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
    public ResponseBean accept(@RequestBody OpsForOrderMsg msg) {
        if(msg == null){
            ResponseBean response = new ResponseBean(400, "参数错误", null);
            return response;
        }
        String userId = msg.getDriverId();
        ResponseBean response = new ResponseBean(500, userId + "的请求未被处理", null);
        if("groupDispatch".equals(dispatchService.getDispatchedMethod())){
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
        else if("singleDispatch".equals(dispatchService.getDispatchedMethod())){
            //删除该订单的已分配过的司机的记录信息
            UnsettledOrder order = infoCacheService.getUOrder(msg.getOrderId());
            infoCacheService.deleteDispatchedSet(msg.getOrderId());
            infoCacheService.deleteUOrder(msg.getOrderId());
            response.setMsg("处理完毕");
            response.setCode(200);
            response.setData(order);
            return response;
        }
        else{
            response.setMsg("内部服务器派单方法配置错误");
            return response;
        }
    }

    /**
     * 拒绝订单
     * @param msg
     * @return
     */
    @PostMapping(value = "/refuse")
    public ResponseBean refuseOrder(@RequestBody OpsForOrderMsg msg){
        if(msg == null){
            ResponseBean response = new ResponseBean(400, "参数错误", null);
            return response;
        }
        String userId = msg.getDriverId();
        ResponseBean response = new ResponseBean(500, userId + "的请求未被处理", null);

        //由于传递的信息都是最小化的，在接单消息中缺少必要的登录信息，故司机拒绝单后需要再次调用登录接口

        //需要再次提交派单申请
        UnsettledOrder order = infoCacheService.getUOrder(msg.getOrderId());
        try{
            response = passengerDispatchController.submit(order);
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
