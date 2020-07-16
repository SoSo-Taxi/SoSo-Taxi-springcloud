package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.bean.MinimizedDriver;
import com.apicaller.sosotaxi.bean.UnsettledOrder;
import com.apicaller.sosotaxi.entity.GeoPoint;

import java.util.Set;

public interface DispatchService {

    /**
     * 通过详细信息更新司机hash
     * @param city
     * @param type
     * @param driverId
     * @param point
     * @return
     * @throws Exception
     */
    Boolean updateDriverField(String city, int type, String driverId, GeoPoint point) throws Exception;

    /**
     * 更新司机hash
     * @param driver
     * @param point
     * @return
     * @throws Exception
     */
    Boolean updateDriverField(MinimizedDriver driver, GeoPoint point) throws Exception;

    /**
     * 通过司机id删除hash中的field
     * @param driverId
     * @return
     * @throws Exception
     */
    void deleteDriverField(String driverId) throws Exception;

    /**
     * 通过司机id更新司机位置
     * @param driverId
     * @param point
     * @throws Exception
     */
    void updatePoint(String driverId, GeoPoint point) throws Exception;

    /**
     * 更新未分配订单hash
     * @param orderId
     * @param uOrder
     */
    void updateUOrderField(String orderId, UnsettledOrder uOrder);

    /**
     * 获取未分配订单
     * @param orderId
     * @return
     */
    UnsettledOrder getUOrder(String orderId);

    /**
     * 删除订单
     * @param orderId
     */
    void deleteUOrder(String orderId);

    /**
     * 获取特定城市特定类型的司机集合
     * @param city
     * @param type
     * @return
     */
    Set<String> getHashByPattern(String city, int type);
}
