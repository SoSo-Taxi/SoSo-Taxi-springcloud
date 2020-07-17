package com.apicaller.sosotaxi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PulseMsg implements Serializable {

    /**
     * 请求执行的功能类型
     */
    public static enum RequestType{
        /**
         * 获取分配的订单
         */
        getOrders,
        /**
         * 接受订单
         */
        acceptOrder
    }

    /**
     * 请求类型
     */
    String request;

    /**
     * 位置信息
     */
    GeoPoint point;

    /**
     * 最小化司机信息
     */
    MinimizedDriver driver;

    /**
     * 按照请求类型进行相应的信息补充
     */
    String detail;
}
