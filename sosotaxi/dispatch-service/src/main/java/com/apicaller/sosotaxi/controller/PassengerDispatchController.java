package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.MinimizedDriver;
import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.service.impl.DispatchServiceImpl;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dispatch/passenger")
public class PassengerDispatchController {

    @Resource
    InfoCacheServiceImpl infoCacheService;

    @Resource
    DispatchServiceImpl dispatchService;

    /**
     * 提交订单
     * @param order
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/submit")
    public ResponseBean submit(@RequestBody UnsettledOrder order) throws Exception {
        //通用的response
        if(order == null){
            ResponseBean response = new ResponseBean(400,"请求参数错误",null);
            return response;
        }
        String userId = order.getPassengerId();
        ResponseBean response = new ResponseBean(500,userId+"的请求未被处理",order);
        //单人分配策略
        if("singleDispatch".equals(dispatchService.getDispatchedMethod())){
            response.setCode(200);
            MinimizedDriver driver = null;
            try{
                //登记该订单以作备用，在被接单时删除，在被再次接单时被调用
                infoCacheService.updateUOrderField(order.getOrderId(),order);
                driver = dispatchService.dispatch(order);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("在乘客接口中分配司机时出错");
            }
            if(driver != null){
                response.setData(driver);
                response.setMsg("成功分配到司机");
                return response;
            }
            else{
                response.setMsg("当前时间无可用司机，请稍后再次请求，该订单已被删除");
                //删除订单信息
                infoCacheService.deleteUOrder(order.getOrderId());
                //删除分配司机信息
                infoCacheService.deleteDispatchedSet(order.getOrderId());
                return response;
            }
        }
        //抢单分配策略
        else if("groupDispatch".equals(dispatchService.getDispatchedMethod())){

            try{
                infoCacheService.updateUOrderField(order.getOrderId(), order);
                response.setCode(200);
                response.setMsg("请求添加成功");
                response.setData(order.getOrderId());
                return response;
            }catch (Exception e){
                e.printStackTrace();
                return response;
            }
        }
        //未开发分配策略
        else{
            response.setCode(500);
            response.setMsg("内部服务器配置错误");
            return response;
        }
    }

    /**
     * 暂时不要使用这个，会再写一个管理类，顺带包含查询方法
     * @param orderId
     * @return
     */
    @GetMapping(value = "/inquire")
    public ResponseBean inquire(@RequestParam(value="passengerId") String orderId){
        ResponseBean response = new ResponseBean(500,"查询失败",null);
        try{
            //TODO:查询Mysql数据库的订单状态，不是查redis！！
            Boolean accepted = true;
            response.setCode(200);
            response.setMsg("查询成功");
            //暂时拿来测试
            if(infoCacheService == null){
                response.setMsg("找不到Bean");
            }
            response.setData(accepted);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return response;
        }
    }
}
