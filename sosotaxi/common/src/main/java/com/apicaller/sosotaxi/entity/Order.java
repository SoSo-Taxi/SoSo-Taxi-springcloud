package com.apicaller.sosotaxi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 用户预约的时间 */
    private Date appointedTime;

    /** 出发时间 */
    private Date departTime;

    /** 到达时间 */
    private Date arriveTime;

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
    private int status;

    /** 订单中，司机获得的评分 */
    private Double driverRate;

    /** 订单中，乘客获得的评分 */
    private Double passengerRate;

    /** 订单路程 */
    private Double distance;

    /** 司机Id */
    private Long driverId;

    /** 乘客Id */
    private Long passengerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }

        Order order = (Order) o;

        if (getCity() != null ? !getCity().equals(order.getCity()) : order.getCity() != null) {
            return false;
        }
        if (getDepartPoint() != null ? !getDepartPoint().equals(order.getDepartPoint()) : order.getDepartPoint() != null) {
            return false;
        }
        if (getDestPoint() != null ? !getDestPoint().equals(order.getDestPoint()) : order.getDestPoint() != null) {
            return false;
        }
        if (getCreateTime() != null ? !getCreateTime().equals(order.getCreateTime()) : order.getCreateTime() != null) {
            return false;
        }
        if (getDepartName() != null ? !getDepartName().equals(order.getDepartName()) : order.getDepartName() != null) {
            return false;
        }
        if (getDestName() != null ? !getDestName().equals(order.getDestName()) : order.getDestName() != null) {
            return false;
        }
        if (getServiceType() != null ? !getServiceType().equals(order.getServiceType()) : order.getServiceType() != null) {
            return false;
        }
        return getPassengerId() != null ? getPassengerId().equals(order.getPassengerId()) : order.getPassengerId() == null;
    }

    @Override
    public int hashCode() {
        int result = getCity() != null ? getCity().hashCode() : 0;
        result = 31 * result + (getDepartPoint() != null ? getDepartPoint().hashCode() : 0);
        result = 31 * result + (getDestPoint() != null ? getDestPoint().hashCode() : 0);
        result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
        result = 31 * result + (getDepartName() != null ? getDepartName().hashCode() : 0);
        result = 31 * result + (getDestName() != null ? getDestName().hashCode() : 0);
        result = 31 * result + (getServiceType() != null ? getServiceType().hashCode() : 0);
        result = 31 * result + (getPassengerId() != null ? getPassengerId().hashCode() : 0);
        return result;
    }
}