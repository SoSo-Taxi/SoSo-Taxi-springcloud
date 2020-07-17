package com.apicaller.sosotaxi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.service.DispatchService;
import com.apicaller.sosotaxi.utils.MapUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class DispatchServiceImpl implements DispatchService {

    @Resource
    private InfoCacheServiceImpl infoCacheService;

    //5s处理一次
    @Override
    @Scheduled(fixedDelay = 5000)
    public void dispatch(){
        List<UnsettledOrder> orders = infoCacheService.getAllUOrder();
        Set<String> driverIds = infoCacheService.getAllDrivers();
        infoCacheService.deleteDurationSets(infoCacheService.getAllDurationSetKeys());

        //TODO:将该方法封装到infoCacheService
        for(String driverId:driverIds){
            infoCacheService.clearDispatch(driverId);
        }

        for (UnsettledOrder order:orders) {
            Set<String> drivers = infoCacheService.getHashByPattern(order.getCity(),order.getType());

            //排序
            for(String driverId:drivers){
                GeoPoint point = infoCacheService.getDriverPosition(driverId);
                try{
                    String result = MapUtil.getDirection(point, order.getOriginPoint());
                    JSONObject resultJson = JSONObject.parseObject(result);
                    String durationStr = MapUtil.getDuration(resultJson);
                    double duration = Double.parseDouble(durationStr);
                    infoCacheService.updateDurationSet(order.getOrderId(),driverId,duration);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            Set<String> driversToDispatch = infoCacheService.getDriverIdByRank(order.getOrderId());

            //TODO:司机的分派单上的订单应当按照距离排列
            //分配
            for(String driverId:driversToDispatch){
                infoCacheService.updateDispatch(driverId, order);
            }
        }
    }
}
