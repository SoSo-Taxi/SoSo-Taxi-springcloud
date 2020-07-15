package com.apicaller.sosotaxi.entity;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 订单实体类
 *
 * @author 骆荟州
 * @createTime  2020-07-14 23:11:58
 */
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 825899775666216793L;
    
    private Long orderId;
    
    private String city;
    
    private GeoPoint departPoint;
    
    private GeoPoint destPoint;
    
    private Date time;
    
    private String departName;
    
    private String destName;

    /** 服务类型，经济型、舒适型等 */
    private Short carType;
    
    private Double cost;

    /** 积分减免 */
    private Double pointDiscount;

    /** 优惠券减免 */
    private Double couponDiscount;
    
    private Short status;

    private Double distance;

    private Long driverId;
    
    private Long passengerId;


}