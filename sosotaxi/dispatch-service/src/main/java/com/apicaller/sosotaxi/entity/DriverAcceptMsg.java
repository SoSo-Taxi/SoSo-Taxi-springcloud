package com.apicaller.sosotaxi.entity;

import lombok.Data;

@Data
public class DriverAcceptMsg {

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
