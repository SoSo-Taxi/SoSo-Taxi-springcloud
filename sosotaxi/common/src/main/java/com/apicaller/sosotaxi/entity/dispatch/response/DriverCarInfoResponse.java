package com.apicaller.sosotaxi.entity.dispatch.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单发配成功后，前端可获取该订单对应的司机信息和车辆信息。
 * 此为返回给前端的信息的类。
 *
 * @author: 骆荟州
 * @createTime: 2020/7/16 3:18 下午
 * @updateTime:
 */
@Data
public class DriverCarInfoResponse implements Serializable {
    private static final long serialVersionUID = -4484485641L;

    private String driverId;

    private String driverName;

    private String phoneNumber;

    private String avatarPath;

    private Short serviceType;

    private String carBrand;

    private String carModel;

    private String carColor;

    private String licensePlate;

}
