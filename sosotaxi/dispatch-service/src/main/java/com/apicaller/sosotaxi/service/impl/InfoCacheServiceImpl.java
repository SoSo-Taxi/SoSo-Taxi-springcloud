package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.entity.MinimizedDriver;
import com.apicaller.sosotaxi.entity.UnsettledOrder;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.service.InfoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

//TODO:添加事务管理，防止严重错误的产生
@Component
public class InfoCacheServiceImpl implements InfoCacheService {

    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Resource
    private StringRedisTemplate sRedisTemplate;

    /**
     * 订单列表
     */
    private static final String U_ORDER_HASH_KEY = "uOrderHash";

    /**
     * 订单分配表
     */
    private static final String DISPATCH_HASH_KEY = "dispatchHash";

    /**
     * 司机索引表
     */
    private static final String DRIVER_HASH_KEY = "driversIndexHash";

    /**
     * 每个订单分配给的司机数
     */
    private static final long ORDER_ASSIGN_COUNT = 8;

    /**
     * 获取司机信息
     * @param driverId
     * @return
     */
    @Override
    public MinimizedDriver getDriver(String driverId){
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        String key = sHashOperations.get(DRIVER_HASH_KEY, driverId);
        String city = key.substring(key.indexOf("_")+1, key.lastIndexOf("_"));
        String typeStr = key.substring(key.lastIndexOf("_")+1);
        int type = Integer.parseInt(typeStr);
        return new MinimizedDriver(city, type, driverId);
    }

    /**
     * 获取所有当前在线司机的名单
     * @return
     */
    @Override
    public Set<String> getAllDrivers(){
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        return sHashOperations.keys(DRIVER_HASH_KEY);
    }

    /**
     * 检查某司机是否在线
     * @param driverId
     * @return
     */
    @Override
    public Boolean hasDriver(String driverId){
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        return sHashOperations.keys(DRIVER_HASH_KEY).contains(driverId);
    }

    /**
     * 通过详细信息更新司机hash
     * @param city
     * @param type
     * @param driverId
     * @param point
     * @return 是否是新创建的hash
     * @throws Exception
     */
    @Override
    public Boolean updateDriverField(String city, int type, String driverId, GeoPoint point) throws Exception {
        HashOperations<String, String, GeoPoint> gHashOperations = redisTemplate.opsForHash();
        String hashKey = "driverHash_"+city+"_"+Integer.toString(type);
        Boolean exist = null;
        try{
            exist = redisTemplate.hasKey(hashKey);
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("redisTemplate注入失败");
        }
        gHashOperations.put(hashKey, driverId, point);
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        sHashOperations.put(DRIVER_HASH_KEY,driverId,hashKey);
        return exist != null && !exist;
    }

    /**
     * 更新司机hash
     * @param driver
     * @param point
     * @return
     * @throws Exception
     */
    @Override
    public Boolean updateDriverField(MinimizedDriver driver, GeoPoint point) throws Exception{
        return updateDriverField(driver.getCity(),driver.getType(),driver.getDriverId(),point);
    }

    /**
     * 获取司机位置
     * @param driverId
     * @return
     */
    @Override
    public GeoPoint getDriverPosition(String driverId){
        HashOperations<String, String, GeoPoint> gHashOperations = redisTemplate.opsForHash();
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        String key = sHashOperations.get(DRIVER_HASH_KEY, driverId);
        return gHashOperations.get(key, driverId);
    }

    /**
     * 通过司机id删除field
     * @param driverId
     * @throws Exception
     */
    @Override
    public void deleteDriverField(String driverId) throws Exception {
        HashOperations<String, String, GeoPoint> gHashOperations = redisTemplate.opsForHash();
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        String hashKey = sHashOperations.get(DRIVER_HASH_KEY,driverId);
        if(hashKey!=null){
            gHashOperations.delete(hashKey, driverId);
        }
        else{
            throw new Exception("删除司机信息缓存时发现信息不存在");
        }
        sHashOperations.delete(DRIVER_HASH_KEY,driverId);
    }

    /**
     * 通过司机id更新位置，性能比通过完整信息更新差，优先考虑使用updateDriverHash
     * @param driverId
     * @param point
     */
    @Override
    public void updatePoint(String driverId, GeoPoint point){
        HashOperations<String, String, String> sHashOperations = sRedisTemplate.opsForHash();
        String hashKey = sHashOperations.get(DRIVER_HASH_KEY, driverId);
        HashOperations<String, String, GeoPoint> gHashOperations = redisTemplate.opsForHash();
        gHashOperations.put(hashKey, driverId, point);
    }

    /**
     * 更新订单hash
     * @param orderId
     * @param uOrder
     */
    @Override
    public void updateUOrderField(String orderId, UnsettledOrder uOrder){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        operations.put(U_ORDER_HASH_KEY, orderId, uOrder);
    }

    /**
     * 获取未分配订单
     * @param orderId
     * @return
     */
    @Override
    public UnsettledOrder getUOrder(String orderId){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        return operations.get(U_ORDER_HASH_KEY, orderId);
    }

    /**
     * 获取所有未分配订单
     * @return
     */
    @Override
    public List<UnsettledOrder> getAllUOrder(){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        return operations.values(U_ORDER_HASH_KEY);
    }

    /**
     * 查看某订单是否存在
     * @param orderId
     * @return
     */
    @Override
    public Boolean hasUOrder(String orderId){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        return operations.keys(U_ORDER_HASH_KEY).contains(orderId);
    }

    /**
     * 删除订单
     * @param orderId
     */
    @Override
    public void deleteUOrder(String orderId){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        operations.delete(U_ORDER_HASH_KEY, orderId);
    }

    /**
     * 获取特定城市特定类型的司机集合
     * @param city
     * @param type
     * @return
     */
    @Override
    public Set<String> getHashByPattern(String city, int type){
        HashOperations<String, String, GeoPoint> gHashOperations = redisTemplate.opsForHash();
        return gHashOperations.keys("driverHash_"+city+"_"+Integer.toString(type));
    }

    /**
     *更新分配的订单
     * @param driverId
     * @param dispatchedOrders
     */
    @Override
    public void updateDispatch(String driverId, List<UnsettledOrder> dispatchedOrders){
        HashOperations<String, String, List<UnsettledOrder>> operations = redisTemplate.opsForHash();
        operations.put(DISPATCH_HASH_KEY,driverId,dispatchedOrders);
    }

    /**
     * 为某司机添加新可选订单
     * @param driverId
     * @param dispatchedOrder
     */
    @Override
    public void updateDispatch(String driverId, UnsettledOrder dispatchedOrder){
        HashOperations<String, String, List<UnsettledOrder>> operations = redisTemplate.opsForHash();
        List<UnsettledOrder> orders = operations.get(DISPATCH_HASH_KEY, driverId);
        if(orders == null){
            orders = new LinkedList<UnsettledOrder>();
        }
        orders.add(dispatchedOrder);
        //保险起见，先删后加，防止有旧版本的bug
        operations.delete(DISPATCH_HASH_KEY,driverId);
        operations.put(DISPATCH_HASH_KEY,driverId,orders);
    }

    /**
     * 获得被分配的订单
     * @param driverId
     * @return
     */
    @Override
    public List<UnsettledOrder> getDispatch(String driverId){
        HashOperations<String, String, List<UnsettledOrder>> operations = redisTemplate.opsForHash();
        return operations.get(DISPATCH_HASH_KEY, driverId);
    }

    /**
     * 按司机id删除司机的所有订单
     * @param driverId
     */
    @Override
    public void clearDispatch(String driverId){
        HashOperations<String, String, List<UnsettledOrder>> operations = redisTemplate.opsForHash();
        operations.delete(DISPATCH_HASH_KEY, driverId);
    }

    /**
     * 为司机删除特定已分配的订单
     * @param driverId
     * @param orderId
     * @return 订单是否存在
     */
    @Override
    public Boolean deleteDispatch(String driverId, String orderId){
        HashOperations<String, String, List<UnsettledOrder>> operations = redisTemplate.opsForHash();
        List<UnsettledOrder> orders = operations.get(DISPATCH_HASH_KEY, driverId);
        for(UnsettledOrder order:orders){
            if(order.toString() == orderId){
                orders.remove(order);
                return true;
            }
        }
        return false;
    }

    /**
     * 更新时间集合
     * @param orderId
     * @param driverId
     * @param duration
     */
    @Override
    public void updateDurationSet(String orderId, String driverId, double duration){
        ZSetOperations<String, String> operations = sRedisTemplate.opsForZSet();
        String Key = "duration_"+ orderId;
        operations.add(Key, driverId, duration);
    }

    /**
     * 清空某时间集合
     * @param orderId
     * @param driverId
     */
    @Override
    public void clearDurationSet(String orderId, String driverId){
        String Key = "duration_"+ orderId;
        redisTemplate.delete(Key);
    }

    /**
     * 获取所有时间集合key
     * @return
     */
    @Override
    public Set<String> getAllDurationSetKeys(){
        return redisTemplate.keys("duration_*");
    }

    /**
     * 删除所有时间集合
     * @param keys
     */
    @Override
    public void deleteDurationSets(Set<String> keys){
        redisTemplate.delete(keys);
    }

    /**
     * 获取订单分配给的司机名单
     * @param orderId
     * @return
     */
    @Override
    public Set<String> getDriverIdByRank(String orderId){
        try{
            ZSetOperations<String, String> operations = sRedisTemplate.opsForZSet();
            String Key = "duration_"+ orderId;
            return operations.range(Key, 0, ORDER_ASSIGN_COUNT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行接单
     * @param redisTemplatePara
     * @param orderId
     * @return
     */
    public Map<String, Object> acceptOrderSessionCallBack(RedisTemplate redisTemplatePara, String orderId){

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isSuccess", null);
        resultMap.put("order", null);

        /**
         * 是否成功接单通过订单是否为空判断
         */
        UnsettledOrder order = null;

        order = (UnsettledOrder) redisTemplatePara.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //该订单未被接受，可以接受
                if(redisOperations.opsForHash().keys(U_ORDER_HASH_KEY).contains(orderId)){
                    UnsettledOrder order = (UnsettledOrder) redisOperations.opsForHash().get(U_ORDER_HASH_KEY,orderId);
                    redisOperations.opsForHash().delete(U_ORDER_HASH_KEY,orderId);
                    return order;
                }
                //订单已不存在，接单失败
                else{
                    return null;
                }
            }
        });

        if(order == null){
            return resultMap;
        }
        else{
            resultMap.replace("isSuccess",true);
            resultMap.replace("order",order);
            return resultMap;
        }
    }

    /**
     * 尝试接受订单
     * @param orderId
     * @return
     */
    @Override
    public Map<String,Object> acceptOrder(String orderId){
        return acceptOrderSessionCallBack(redisTemplate, orderId);
    }

    /**
     * 测试用，添加
     * @param testKey
     * @param value
     */
    @Override
    public void addTestMsgKey(String testKey, String value) {
        HashOperations<String, String, String> operations = sRedisTemplate.opsForHash();
        operations.put(testKey,"testField",value);
    }

    /**
     * 测试用，删除
     * @param testKey
     */
    @Override
    public void deleteTestMsgKey(String testKey) {
        HashOperations<String, String, String> operations = sRedisTemplate.opsForHash();
        operations.delete(testKey,"testField");
    }
}
