package com.apicaller.sosotaxi.entity.dispatchservice.message;

import com.apicaller.sosotaxi.entity.GeoPoint;
import lombok.Data;

import java.io.Serializable;

@Data
public class DriverUpdateMsg implements Serializable {

    /**
     * 司机id
     */
    private String driverId;

    /**
     * 司机位置信息
     */
    private GeoPoint point;

}
