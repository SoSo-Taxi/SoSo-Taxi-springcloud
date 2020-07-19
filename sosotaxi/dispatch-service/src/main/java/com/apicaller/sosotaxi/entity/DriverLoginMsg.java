package com.apicaller.sosotaxi.entity;

import lombok.Data;

@Data
public class DriverLoginMsg {

    /**
     * 司机所在位置
     */
    private GeoPoint point;

    /**
     * 司机分配必要信息
     */
    private MinimizedDriver driver;

}
