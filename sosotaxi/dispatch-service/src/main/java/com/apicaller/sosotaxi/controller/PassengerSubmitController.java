package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dispatch/passenger/")
public class PassengerSubmitController {
    @Resource
    InfoCacheServiceImpl dispatchService;

    @PostMapping(value = "/submit")
    public ResponseBean submit(@RequestParam(value="passengerId") String userId, @RequestBody UnsettledOrder order){
        ResponseBean response = new ResponseBean(500,userId+"的请求未被处理",order);
        try{
            dispatchService.updateUOrderField(userId, order);
            response.setCode(200);
            response.setMsg("请求添加成功");
            response.setData(order.getOrderId());
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return response;
        }
    }

    @GetMapping(value = "/inquire")
    public ResponseBean inquire(@RequestParam(value="passengerId") String orderId){
        ResponseBean response = new ResponseBean(500,"查询失败",null);
        try{
            //TODO:查询Mysql数据库的订单状态，不是查redis！！
            Boolean accepted = true;
            response.setCode(200);
            response.setMsg("查询成功");
            //暂时拿来测试
            if(dispatchService == null){
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
