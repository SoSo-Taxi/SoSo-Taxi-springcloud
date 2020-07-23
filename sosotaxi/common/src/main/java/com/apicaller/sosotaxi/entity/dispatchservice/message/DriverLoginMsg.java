package com.apicaller.sosotaxi.entity.dispatchservice.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.apicaller.sosotaxi.entity.dispatchservice.MinimizedDriver;
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
