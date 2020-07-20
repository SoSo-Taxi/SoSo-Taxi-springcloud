package com.apicaller.sosotaxi.entity.dispatch.dto;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 前端发起打车请求时通过Json传来到数据。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/16 2:54 下午
 * @updateTime:
 */
@Data
public class GenerateOrderDTO implements Serializable {
    private static final long serialVersionUID = 4484485641L;

    private String city;

    private Long passengerId;

    /** 考虑到可能有预约订单，这里命名为出发时间。*/
    private Date departTime;

    private Short passengerNum;

    private GeoPoint departPoint;

    private GeoPoint destPoint;

    private String departName;

    private String destName;

    private Short serviceType;
}
