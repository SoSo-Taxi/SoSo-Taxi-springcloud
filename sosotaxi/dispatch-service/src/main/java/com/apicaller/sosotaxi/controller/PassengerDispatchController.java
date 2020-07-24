package com.apicaller.sosotaxi.controller;

import com.apicaller.sosotaxi.entity.dispatchservice.MinimizedDriver;
import com.apicaller.sosotaxi.entity.dispatchservice.UnsettledOrder;
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
     * 暂时没用result封装信息，无法知道返回失败的原因
     * 当返回null时，暂时视作该单已被其他司机接受处理
     * 如果调用方不接受result封装可以考虑map封装，出错的可能性比reslut类封装大
     * @param order
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/submit")
    public MinimizedDriver submit(@RequestBody UnsettledOrder order) throws Exception {
        //先细化排除空指针异常
        if(order == null){
            throw new Exception("参数错误");
        }
        String userId = order.getPassengerId();
        MinimizedDriver targetDriver = null;
        //单人分配策略
        if(dispatchService.getDispatchMethod() == DispatchServiceImpl.DispatchMethod.AUTOARRANGE){

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

                targetDriver = driver;
                return targetDriver;
            }
            else{
                //"当前时间无可用司机，请稍后再次请求，该订单已被删除"
                //删除订单信息
                infoCacheService.deleteUOrder(order.getOrderId());
                //删除分配司机信息
                infoCacheService.deleteDispatchedSet(order.getOrderId());
                return null;
            }
        }
        //抢单分配策略
        //暂时用不上，这个版本就先不做了
        else if("groupDispatch".equals(dispatchService.getDispatchMethodStr())) {
//
//            try{
//                infoCacheService.updateUOrderField(order.getOrderId(), order);
//                response.setCode(200);
//                response.setMsg("请求添加成功");
//                response.setData(order.getOrderId());
//                return response;
//            }catch (Exception e){
//                e.printStackTrace();
//                return response;
//            }
//        }
//        //未开发分配策略
//        else{
//            response.setCode(500);
//            response.setMsg("内部服务器配置错误");
//            return response;
//        }
            targetDriver = null;
        }

        return  targetDriver;
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
