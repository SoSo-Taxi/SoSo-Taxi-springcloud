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

    /** 订单Id */
    private Long orderId;

    /** 城市 */
    private String city;

    /** 出发地 */
    private GeoPoint departPoint;

    /** 目的地 */
    private GeoPoint destPoint;

    /** 订单创建时间 */
    private Date createTime;

    /** 出发时间 */
    private Date departTime;

    /** 起点名称 */
    private String departName;

    /** 目的地名称 */
    private String destName;

    /** 服务类型，经济型、舒适型等 */
    private Short serviceType;

    /** 原始费用 */
    private Double cost;

    /** 积分减免 */
    private Double pointDiscount;

    /** 优惠券减免 */
    private Double couponDiscount;

    /** 订单状态 */
    private String status;

    /** 订单评分 */
    private Short rate;

    /** 订单路程 */
    private Double distance;

    /** 司机Id */
    private Long driverId;

    /** 乘客Id */
    private Long passengerId;


}