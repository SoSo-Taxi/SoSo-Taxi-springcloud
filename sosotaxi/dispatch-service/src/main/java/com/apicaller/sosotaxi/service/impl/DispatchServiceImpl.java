package com.apicaller.sosotaxi.service.impl;

import com.apicaller.sosotaxi.bean.MinimizedDriver;
import com.apicaller.sosotaxi.bean.UnsettledOrder;
import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static String uOrderHashKey = "uOrderHash";

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
        HashOperations<String, String, String> sHashOperations = redisTemplate.opsForHash();
        sHashOperations.put("drivers",driverId,hashKey);
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
     * 通过司机id删除field
     * @param driverId
     * @throws Exception
     */
    @Override
    public void deleteDriverField(String driverId) throws Exception {
        HashOperations<String, String, GeoPoint> gHashOperations = redisTemplate.opsForHash();
        HashOperations<String, String, String> sHashOperations = redisTemplate.opsForHash();
        String hashKey = sHashOperations.get("drivers",driverId);
        if(hashKey!=null){
            gHashOperations.delete(hashKey, driverId);
        }
        else{
            throw new Exception("删除司机信息缓存时发现信息不存在");
        }
        sHashOperations.delete("drivers",driverId);
    }

    /**
     * 通过司机id更新位置，性能比通过完整信息更新差，优先考虑使用updateDriverHash
     * @param driverId
     * @param point
     */
    @Override
    public void updatePoint(String driverId, GeoPoint point){
        HashOperations<String, String, String> sHashOperations = redisTemplate.opsForHash();
        String hashKey = sHashOperations.get("drivers", driverId);
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
        operations.put(uOrderHashKey, orderId, uOrder);
    }

    /**
     * 获取未分配订单
     * @param orderId
     * @return
     */
    @Override
    public UnsettledOrder getUOrder(String orderId){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        return operations.get(uOrderHashKey, orderId);
    }

    /**
     * 删除订单
     * @param orderId
     */
    @Override
    public void deleteUOrder(String orderId){
        HashOperations<String, String, UnsettledOrder> operations = redisTemplate.opsForHash();
        operations.delete(uOrderHashKey, orderId);
    }

    /**
     * 获取特定城市特定类型的司机集合
     * @param city
     * @param type
     * @return
     */
    @Override
    public Set<String> getHashByPattern(String city, int type){
        return redisTemplate.keys(city+"_"+Integer.toString(type)+"_*");
    }

}
