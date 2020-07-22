package com.apicaller.sosotaxi.entity.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

@Data
public class OpsForOrderMsg {

    /**
     * 位置信息
     */
    private GeoPoint point;

    /**
     * 司机id
     */
    private String driverId;

    /**
     * 订单id
     */
    private String orderId;
}
