package com.apicaller.sosotaxi.entity.dispatchservice;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UnsettledOrder implements Serializable {

    /**
     * 订单Id
     * 在相应的数据库完整实体类中自增，此处作为参数传入
     */
    private String orderId;

    /**
     * 乘客唯一标识号
     */
    private String passengerId;

    /**
     * 订单起点所在城市
     */
    private String city;

    /**
     * 类型
     */
    private int type;

    /**
     * 起点封装
     */
    private GeoPoint originPoint;

    /**
     * 目的地封装
     */
    private GeoPoint destinationPoint;

    /**
     * 订单创建时间
     * 作为重要参数参与派单分析
     */
    private Date createdTime;

    public UnsettledOrder(){
    //
    }

    public UnsettledOrder(String orderId, String passengerId, String city, int type, GeoPoint originPoint, GeoPoint destinationPoint, Date createdTime){
        this.orderId = orderId;
        this.passengerId = passengerId;
        this.city = city;
        this.type = type;
        this.originPoint = originPoint;
        this.destinationPoint = destinationPoint;
        this.createdTime = createdTime;
    }

    @Override
    public String toString(){
        return orderId;
    }
}
