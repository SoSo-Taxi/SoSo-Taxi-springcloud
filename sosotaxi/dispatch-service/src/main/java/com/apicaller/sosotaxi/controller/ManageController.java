package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.MinimizedDriver;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.service.DispatchService;
import com.apicaller.sosotaxi.service.impl.DispatchServiceImpl;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dispatch/Manager")
public class ManageController {

    @Resource
    InfoCacheServiceImpl infoCacheService;

    @Resource
    DispatchServiceImpl dispatchService;

    /**
     * 改变策略
     * @param method
     * @return
     */
    @GetMapping(value="/changeDispatchMethod")
    public ResponseBean changeMethod(@RequestParam(value="method") String method){
        ResponseBean response = new ResponseBean(500, "执行异常", null);
        if(dispatchService.changeDispatchMethod(method)){
            response.setMsg("成功");
        }
        else{
            response.setMsg("参数错误");
        }
        return response;
    }

    /**
     * 查询分配到的订单
     * 仅当抢单模式时有效
     * @param driverId
     * @return
     */
    @GetMapping(value="/dispatchedOrders")
    public ResponseBean getDisPatchedOrderList(@RequestParam(value="driverId") String driverId){
        ResponseBean response = new ResponseBean(500, "执行异常", null);
        try{
            List<UnsettledOrder> orders = infoCacheService.getDispatch(driverId);
            response.setCode(200);
            response.setMsg("查询到"+driverId+"的可接受订单列表");
            response.setData(orders);
            return response;
        }catch (Exception e){
            response.setCode(500);
            String exception = e.getMessage();
            response.setMsg("内部服务器异常:"+exception);
            return  response;
        }
    }

    static int illegalAccess = 0;

    /**
     * 添加测试数据，仅供测试，正式业务中禁止使用
     * 抢单模式测试数据
     * @param password
     * @return
     */
    @GetMapping(value="/addTestInfo")
    public ResponseBean addTestInfo(@RequestParam(value="password") String password){
        ResponseBean response = new ResponseBean(200, "别用这个！！！", null);
        if(!"wvg0ma1_2fw3ho_drxhwl7ui".equals(password) ||illegalAccess>2){
            illegalAccess+=1;
            return response;
        }
        try{
            //0和1应该不会出现在武汉1型车的查询结果中
            MinimizedDriver driver0 = new MinimizedDriver("ShangHai", 1, "d_test_S_1_0_!");
            MinimizedDriver driver1 = new MinimizedDriver("WuHan", 2, "d_test_W_2_1_!");
            MinimizedDriver driver2 = new MinimizedDriver("WuHan", 1, "d_test_W_1_2_ok");
            MinimizedDriver driver3 = new MinimizedDriver("WuHan", 1, "d_test_W_1_3_ok");
            MinimizedDriver driver4 = new MinimizedDriver("WuHan", 1, "d_test_W_1_4_ok");
            MinimizedDriver driver5 = new MinimizedDriver("WuHan", 1, "d_test_W_1_5_ok");
            MinimizedDriver driver6 = new MinimizedDriver("WuHan", 1, "d_test_W_1_6_ok");
            MinimizedDriver driver7 = new MinimizedDriver("WuHan", 1, "d_test_W_1_7_ok");
            MinimizedDriver driver8 = new MinimizedDriver("WuHan", 1, "d_test_W_1_8_ok");
            MinimizedDriver driver9 = new MinimizedDriver("WuHan", 1, "d_test_W_1_9_ok");
            MinimizedDriver driverLeft = new MinimizedDriver("WuHan",1,"d_test_W_1_left_wrong");

            //0为终点
            GeoPoint point0 = new GeoPoint(39.00116, 115.452562);
            GeoPoint point1 = new GeoPoint(39.01116, 115.452562);
            GeoPoint point2 = new GeoPoint(39.02116, 115.452562);
            GeoPoint point3 = new GeoPoint(39.03116, 115.452562);
            GeoPoint point4 = new GeoPoint(39.04116, 115.452562);

            //暂时没用到创建时间
            Date time0 = new Date(0);

            //假订单
            UnsettledOrder order0 = new UnsettledOrder("o_test_S_1_0", "p_test_0", "ShangHai", 1, point1, point0, time0);
            UnsettledOrder order1 = new UnsettledOrder("o_test_W_2_1", "p_test_1", "WuHan", 2, point1, point0, time0);
            UnsettledOrder order2 = new UnsettledOrder("o_test_W_1_2", "p_test_2", "WuHan", 1, point1, point0, time0);

            //调用服务以使用分配
            infoCacheService.updateDriverField(driver0,point2);
            infoCacheService.updateDriverField(driver1,point2);
            infoCacheService.updateDriverField(driver2,point2);
            infoCacheService.updateDriverField(driver3,point3);
            infoCacheService.updateDriverField(driver4,point3);
            infoCacheService.updateDriverField(driver5,point3);
            //在中间插距离最大的数据以保证测试结果不受添加顺序影响
            infoCacheService.updateDriverField(driverLeft,point4);
            infoCacheService.updateDriverField(driver6,point3);
            infoCacheService.updateDriverField(driver7,point3);
            infoCacheService.updateDriverField(driver8,point3);
            infoCacheService.updateDriverField(driver9,point3);

            infoCacheService.updateUOrderField(order0.getOrderId(),order0);
            infoCacheService.updateUOrderField(order1.getOrderId(),order1);
            infoCacheService.updateUOrderField(order2.getOrderId(),order2);

            response.setCode(200);
            response.setMsg("生成假数据成功");
            return response;
        }catch (Exception e){
            response.setCode(500);
            String exception = e.getMessage();
            response.setMsg("内部服务器异常:"+exception);
            return  response;
        }
    }

    /**
     * flushall
     * @param password
     * @return
     */
    @GetMapping(value = "/deleteAll")
    public ResponseBean flushAllCaches(@RequestParam(value="password") String password){
        ResponseBean response = new ResponseBean(200, "别用这个！！！", null);
        if(!"hfo0209_f9u0bw_cam28f2mg".equals(password) ||illegalAccess>2){
            illegalAccess+=1;
            return response;
        }
        try{
            if(infoCacheService.deleteAll()){
                response.setCode(200);
                response.setMsg("清空全部数据");
            }
            else{
                response.setCode(200);
                response.setMsg("清空失败");
            }
            return response;
        }catch (Exception e){
            response.setCode(500);
            String exception = e.getMessage();
            response.setMsg("内部服务器异常:"+exception);
            return  response;
        }
    }

    /**
     * 获取某订单的可接受司机
     * 仅当抢单模式时有效
     * @param orderId
     * @return
     */
    @GetMapping(value="/dispatchedDrivers")
    public ResponseBean getDisPatchedDriversList(@RequestParam(value="orderId") String orderId){
        ResponseBean response = new ResponseBean(500, "执行异常", null);
        try{
            Set<String> drivers = infoCacheService.getDriverIdByRank(orderId, dispatchService.getArrangeCount());
            response.setCode(200);
            response.setMsg("查询到"+orderId+"的可接受司机列表");
            response.setData(drivers);
            return response;
        }catch (Exception e){
            response.setCode(500);
            String exception = e.getMessage();
            response.setMsg("内部服务器异常:"+exception);
            return  response;
        }
    }
}
