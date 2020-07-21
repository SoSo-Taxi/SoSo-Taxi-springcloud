package com.apicaller.sosotaxi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.service.DispatchService;
import com.apicaller.sosotaxi.utils.MapUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
public class DispatchServiceImpl implements DispatchService {

    /**
     * 派单模式
     */
    public enum dispatchMethod{
        /**
         * 抢单模式
         */
        GROUPDISPATCH,
        /**
         * 自动安排模式
         */
        AUTOARRANGE
    }

    /**
     * 当前策略
     */
    private dispatchMethod currentMethod = dispatchMethod.GROUPDISPATCH;

    /**
     * 更改策略
     * @param method
     * @return
     */
    public Boolean changeDispatchMethod(String method){
        if(method.equals(dispatchMethod.AUTOARRANGE.toString())){
            currentMethod = dispatchMethod.AUTOARRANGE;
        }
        else if(method.equals(dispatchMethod.GROUPDISPATCH.toString())){
            currentMethod = dispatchMethod.GROUPDISPATCH;
        }
        else{
            return false;
        }
        return true;
    }

    /**
     * 抢单模式下一单的最大分配司机数
     */
    static private int arrangeCount = 8;

    /**
     * 设置最大分配司机数
     * 仅当模式为抢单模式时有效
     * @param arrangeCount
     */
    public void setArrangeCount(int arrangeCount){
        DispatchServiceImpl.arrangeCount = arrangeCount;
    }

    /**
     * 设置最大分配司机数
     * @return
     */
    public int getArrangeCount(){
        return DispatchServiceImpl.arrangeCount;
    }

    @Resource
    private InfoCacheServiceImpl infoCacheService;

    @Resource
    private MapUtils mapUtils;

    //5s处理一次
    @Override
    @Scheduled(fixedDelay = 5000)
    public void dispatch() throws Exception {
        //仅当当前策略为抢单模式时才执行定时处理方法
        if(currentMethod!=dispatchMethod.GROUPDISPATCH){
            return;
        }
        List<UnsettledOrder> orders = infoCacheService.getAllUOrder();
        Set<String> driverIds = infoCacheService.getAllDrivers();
        infoCacheService.deleteDurationSets(infoCacheService.getAllDurationSetKeys());

        //TODO:将该方法封装到infoCacheService
        //并且将清除的方式更改，防止用户在处理时读不到数据
        //现在的效果只能做演示，绝对绝对不能留到最后！！
        for(String driverId:driverIds){
            infoCacheService.clearDispatch(driverId);
        }

        for (UnsettledOrder order:orders) {
            Set<String> drivers = infoCacheService.getHashByPattern(order.getCity(),order.getType());

            //排序
            for(String driverId:drivers){
                GeoPoint point = infoCacheService.getDriverPosition(driverId);
                try{
                    String result = mapUtils.getDirection(point, order.getOriginPoint());
                    double duration = MapUtils.getDuration(result);
                    infoCacheService.updateDurationSet(order.getOrderId(),driverId,duration);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            Set<String> driversToDispatch = null;
            try {
                driversToDispatch = infoCacheService.getDriverIdByRank(order.getOrderId(), arrangeCount);
            }catch (Exception e){
                e.printStackTrace();
            }
            //TODO:司机的分派单上的订单应当按照距离排列
            //分配
            if(driversToDispatch==null){
                continue;
            }
            for(String driverId:driversToDispatch){
                infoCacheService.updateDispatch(driverId, order);
            }
        }
    }
}
