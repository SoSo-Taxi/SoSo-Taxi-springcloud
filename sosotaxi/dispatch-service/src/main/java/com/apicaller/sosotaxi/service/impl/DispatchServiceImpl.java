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

    @Resource
    private InfoCacheServiceImpl infoCacheService;

    //5s处理一次
    @Override
    @Scheduled(fixedDelay = 5000)
    public void dispatch() throws Exception {
        List<UnsettledOrder> orders = infoCacheService.getAllUOrder();
        Set<String> driverIds = infoCacheService.getAllDrivers();
        infoCacheService.deleteDurationSets(infoCacheService.getAllDurationSetKeys());

        //TODO:将该方法封装到infoCacheService
        //并且将清除的方式更改，防止用户在处理时读不到数据
        for(String driverId:driverIds){
            infoCacheService.clearDispatch(driverId);
        }

        for (UnsettledOrder order:orders) {
            Set<String> drivers = infoCacheService.getHashByPattern(order.getCity(),order.getType());

            //排序
            for(String driverId:drivers){
                GeoPoint point = infoCacheService.getDriverPosition(driverId);
                try{
                    String result = MapUtils.getDirection(point, order.getOriginPoint());
                    JSONObject resultJson = JSONObject.parseObject(result);
                    String durationStr = MapUtils.getDuration(resultJson);
                    double duration = Double.parseDouble(durationStr);
                    infoCacheService.updateDurationSet(order.getOrderId(),driverId,duration);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            Set<String> driversToDispatch = null;
            try {
                driversToDispatch = infoCacheService.getDriverIdByRank(order.getOrderId());
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
