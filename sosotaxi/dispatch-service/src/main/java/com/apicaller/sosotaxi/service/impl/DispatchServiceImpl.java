package com.apicaller.sosotaxi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.apicaller.sosotaxi.entity.MinimizedDriver;
import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.service.DispatchService;
import com.apicaller.sosotaxi.utils.MapUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

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
    private dispatchMethod currentMethod = dispatchMethod.AUTOARRANGE;

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
     * 以字符串的形式返回当前策略
     * 后期分配策略的部分稳定后可以换成返回枚举，因为目前还要调整所以暂时用字符串减少依赖
     * @return
     */
    public String getDispatchedMethod(){
        if(currentMethod == dispatchMethod.AUTOARRANGE){
            return "singleDispatch";
        }
        else if(currentMethod == dispatchMethod.GROUPDISPATCH){
            return "groupDispatch";
        }
        else{
            return "still developing ...";
        }
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


    /**
     * 立即获取相应订单最合适的司机
     * @param order
     * @return 分配的司机
     * 如果为空代表当前无合适司机，请等待一段时间后重试
     * @throws Exception
     */
    @Override
    public MinimizedDriver dispatch(UnsettledOrder order) throws Exception {
        if(order == null){
            throw new Exception("order不存在，传入了错误的参数");
        }
        String city = order.getCity();
        int type = order.getType();
        Set<String> drivers = infoCacheService.getHashByPattern(city, type);
        if(drivers == null){
            return null;
        }
        HashMap<String, Integer> driversMap = new HashMap<>();
//        String optimalDriver = null;
//        Integer minTime = Integer.MAX_VALUE;
        for(String driverId:drivers){
            GeoPoint point = null;
            try{
                point = infoCacheService.getDriverPosition(driverId);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("缓存中不存在该类型司机信息");
            }
            String result = null;
            try{
                if(point != null){
                    result = mapUtils.getDirection(point, order.getOriginPoint());
                }
                else{
                    continue;
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("在分配算法中调用地图api时出错");
            }
            Integer durection = null;
            try{
                if(result != null){
                    durection = MapUtils.getDistance(result);
                }
                else{
                    continue;
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("在分配算法中解析api返回结果时出错");
            }
            driversMap.put(driverId, durection);
        }
        if(driversMap.isEmpty()){
            return null;
        }
        else{
            //排序
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(driversMap.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            MinimizedDriver targetDriver = null;

            String orderId = order.getOrderId();

            //遍历排序过的集合
            for(Map.Entry<String, Integer> entry:list){
                String driverId = entry.getKey();
                //已经尝试过分配给该司机，不应该再次尝试
                if(infoCacheService.getDispatchedSet(orderId) != null && infoCacheService.getDispatchedSet(orderId).contains(driverId)){
                    continue;
                }
                //分配成功
                //dispatchDriver方法会自动注销该司机，所以先拿出来
                targetDriver = infoCacheService.getDriver(driverId);
                if(infoCacheService.dispatchDriver(driverId)){
                    //记录该司机已经尝试过接单
                    infoCacheService.addDriverToDispatchedSet(orderId,driverId);
                    break;
                }
            }
            return targetDriver;
        }
    }
}
