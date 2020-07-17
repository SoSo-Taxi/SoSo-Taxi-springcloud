package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.ResponseBean;
import com.apicaller.sosotaxi.service.impl.InfoCacheServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/dispatch/passenger/{passengerId}")
public class PassengerSubmitController {
    @Resource
    InfoCacheServiceImpl dispatchService;

    @PostMapping(value = "/submit")
    public ResponseBean submit(@PathParam(value="passengerId") String userId, @RequestBody UnsettledOrder order){
        ResponseBean response = new ResponseBean();
        response.setCode(500);
        response.setMsg("请求未被处理");
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
    public ResponseBean inquire(String orderId){
        ResponseBean response = new ResponseBean();
        response.setCode(500);
        response.setMsg("查询失败");
        try{
            Boolean accepted = true;
            response.setCode(200);
            response.setMsg("查询成功");
            response.setData(accepted);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return response;
        }
    }
}
